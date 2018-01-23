/*******************************************************************************
 * Copyright (c) 2017, 2018 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at http://eclipse.org/legal/epl-2.0
 * or the Apache License, Version 2.0 which accompanies this distribution
 * and is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License, v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception [1] and GNU General Public
 * License, version 2 with the OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] http://openjdk.java.net/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/
package dwarf.tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.LongFunction;

import org.eclipse.cdt.utils.elf.Elf;

public class DwarfScanner {

	private static final class Abbreviation {

		private static Abbreviation find(Abbreviation[] abbreviations, long code) {
			Abbreviation abbreviation = null;
			int lo = 0;
			int hi = abbreviations.length;
			int mid;

			if (lo < code && code <= hi) {
				// if codes are contiguous starting at 1,
				// we'll find it on the first test
				mid = (int) code - 1;
			} else {
				mid = hi / 2;
			}

			// do a binary search
			for (; lo < hi; mid = (lo + hi) >>> 1) {
				abbreviation = abbreviations[mid];

				if (code == abbreviation.code) {
					return abbreviation;
				} else if (code > abbreviation.code) {
					lo = mid + 1;
				} else {
					hi = mid;
				}
			}

			return null;
		}

		static LongFunction<Abbreviation> readFrom(DataSource data) {
			List<Abbreviation> abbreviations = new ArrayList<>();

			while (data.hasRemaining()) {
				long code = data.getUDATA();

				if (code == 0) {
					break;
				}

				long tag = data.getUDATA();
				boolean hasChildren = data.getU1() != 0;
				Abbreviation entry = new Abbreviation(code, tag, hasChildren);

				// attributes
				for (;;) {
					long name = data.getUDATA();
					long form = data.getUDATA();

					if (name != 0 && form != 0) {
						entry.addAttribute(name, form);
					} else {
						break;
					}
				}

				abbreviations.add(entry);
			}

			Abbreviation[] list = abbreviations.toArray(new Abbreviation[abbreviations.size()]);

			Arrays.sort(list, Comparator.comparingLong(abbreviation -> abbreviation.code));

			return code -> find(list, code);
		}

		private final List<AttributeReader> attributes;

		private final long code;

		final boolean hasChildren;

		final int tag;

		private Abbreviation(long code, long tag, boolean hasChildren) {
			super();
			this.attributes = new LinkedList<>();
			this.code = code;
			this.hasChildren = hasChildren;
			this.tag = (int) tag;
		}

		private void addAttribute(long attribute, long form) {
			if ((0 < attribute && attribute <= Integer.MAX_VALUE) && (0 < form && form <= Integer.MAX_VALUE)) {
				attributes.add(AttributeReader.create((int) attribute, (int) form));
			} else {
				throw new IllegalArgumentException("attribute=" + attribute + " form=" + form);
			}
		}

		void readAttributes(DwarfRequestor requestor, DataSource data) {
			for (AttributeReader attribute : attributes) {
				attribute.read(requestor, data);
			}
		}

		@Override
		public String toString() {
			return "abbrev(" + code + ") tag=" + tag;
		}

	}

	private abstract static class AttributeReader {

		private static final class Address extends AttributeReader {

			Address(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long address = data.getAddress();

				requestor.acceptAddress(attribute, form, address);
			}

		}

		private static final class Block extends AttributeReader {

			Block(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			final void read(DwarfRequestor requestor, DataSource data) {
				long length;

				switch (form) {
				case DwarfForm.DW_FORM_block:
					length = data.getUDATA();
					break;
				case DwarfForm.DW_FORM_block1:
					length = data.getU1();
					break;
				case DwarfForm.DW_FORM_block2:
					length = data.getU2();
					break;
				case DwarfForm.DW_FORM_block4:
					length = data.getU4();
					break;
				default:
					throw unexpectedForm();
				}

				byte[] block = new byte[checkUInt(length)];

				data.getBlock(block);

				requestor.acceptBlock(attribute, form, block);
			}

		}

		private static final class Constant extends AttributeReader {

			Constant(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long value;

				switch (form) {
				case DwarfForm.DW_FORM_data1:
					value = data.getU1();
					break;
				case DwarfForm.DW_FORM_data2:
					value = data.getU2();
					break;
				case DwarfForm.DW_FORM_data4:
					value = data.getU4();
					break;
				case DwarfForm.DW_FORM_data8:
					value = data.getU8();
					break;
				case DwarfForm.DW_FORM_sdata:
					value = data.getSDATA();
					break;
				case DwarfForm.DW_FORM_udata:
					value = data.getUDATA();
					break;
				default:
					throw unexpectedForm();
				}

				requestor.acceptConstant(attribute, form, value);
			}

		}

		private static final class Expression extends AttributeReader {

			Expression(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			final void read(DwarfRequestor requestor, DataSource data) {
				long length;

				switch (form) {
				case DwarfForm.DW_FORM_exprloc:
					length = data.getUDATA();
					break;
				default:
					throw unexpectedForm();
				}

				byte[] expression = new byte[checkUInt(length)];

				data.getBlock(expression);

				requestor.acceptExpression(attribute, form, expression);
			}

		}

		private static final class Flag extends AttributeReader {

			Flag(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				boolean flag;

				switch (form) {
				case DwarfForm.DW_FORM_flag:
					flag = data.getU1() != 0;
					break;
				case DwarfForm.DW_FORM_flag_present:
					flag = true;
					break;
				default:
					throw unexpectedForm();
				}

				requestor.acceptFlag(attribute, form, flag);
			}

		}

		private static final class Indirect extends AttributeReader {

			Indirect(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				int actualForm = checkUInt(data.getUDATA());
				AttributeReader actualReader = create(attribute, actualForm);

				actualReader.read(requestor, data);
			}

		}

		private static final class Reference extends AttributeReader {

			Reference(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long offset;

				switch (form) {
				case DwarfForm.DW_FORM_ref1:
					offset = data.getU1();
					break;
				case DwarfForm.DW_FORM_ref2:
					offset = data.getU2();
					break;
				case DwarfForm.DW_FORM_ref4:
					offset = data.getU4();
					break;
				case DwarfForm.DW_FORM_ref8:
				case DwarfForm.DW_FORM_ref_sig8:
					offset = data.getU8();
					break;
				case DwarfForm.DW_FORM_ref_udata:
					offset = data.getUDATA();
					break;
				case DwarfForm.DW_FORM_ref_addr:
				case DwarfForm.DW_FORM_sec_offset:
					offset = data.getOffset();
					break;
				default:
					throw unexpectedForm();
				}

				requestor.acceptReference(attribute, form, offset);
			}

		}

		private static final class Str extends AttributeReader {

			Str(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				String string;

				switch (form) {
				case DwarfForm.DW_FORM_string:
					string = data.getString();
					break;
				case DwarfForm.DW_FORM_strp:
					string = data.lookupString(data.getOffset());
					break;
				default:
					throw unexpectedForm();
				}

				requestor.acceptString(attribute, form, string);
			}

		}

		/**
		 * This class allows us to parse abbreviations using unknown forms.
		 * If the abbreviation is unused this poses no problem.
		 * If it is used, we won't know how to handle the data associated
		 * with the attribute and instead throw an exception.
		 */
		private static final class Unknown extends AttributeReader {

			Unknown(int attribute, int form) {
				super(attribute, form);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				throw new IllegalArgumentException("attribute=" + attribute + " form=" + form);
			}

		}

		static int checkUInt(long value) {
			if (0 <= value && value <= Integer.MAX_VALUE) {
				return (int) value;
			}

			throw new IllegalArgumentException("Not U4: " + value);
		}

		static AttributeReader create(int attribute, int form) {
			switch (form) {
			case DwarfForm.DW_FORM_addr:
				return new Address(attribute, form);

			case DwarfForm.DW_FORM_block:
			case DwarfForm.DW_FORM_block1:
			case DwarfForm.DW_FORM_block2:
			case DwarfForm.DW_FORM_block4:
				return new Block(attribute, form);

			case DwarfForm.DW_FORM_flag:
			case DwarfForm.DW_FORM_flag_present:
				return new Flag(attribute, form);

			case DwarfForm.DW_FORM_data1:
			case DwarfForm.DW_FORM_data2:
			case DwarfForm.DW_FORM_data4:
			case DwarfForm.DW_FORM_data8:
			case DwarfForm.DW_FORM_sdata:
			case DwarfForm.DW_FORM_udata:
				return new Constant(attribute, form);

			case DwarfForm.DW_FORM_string:
			case DwarfForm.DW_FORM_strp:
				return new Str(attribute, form);

			case DwarfForm.DW_FORM_ref1:
			case DwarfForm.DW_FORM_ref2:
			case DwarfForm.DW_FORM_ref4:
			case DwarfForm.DW_FORM_ref8:
			case DwarfForm.DW_FORM_ref_sig8:
			case DwarfForm.DW_FORM_ref_udata:
			case DwarfForm.DW_FORM_sec_offset:
			case DwarfForm.DW_FORM_ref_addr:
				return new Reference(attribute, form);

			case DwarfForm.DW_FORM_exprloc:
				return new Expression(attribute, form);

			case DwarfForm.DW_FORM_indirect:
				return new Indirect(attribute, form);

			default:
				return new Unknown(attribute, form);
			}
		}

		final int attribute;

		final int form;

		AttributeReader(int attribute, int form) {
			super();
			this.attribute = attribute;
			this.form = form;
		}

		abstract void read(DwarfRequestor requestor, DataSource data);

		final IllegalStateException unexpectedForm() {
			return new IllegalStateException("form=" + form);
		}

	}

	public static final int VERSION_MAXIMUM = 4;

	public static final int VERSION_MINIMUM = 2;

	private static void scanTags(DwarfRequestor requestor, DataSource data, LongFunction<Abbreviation> abbreviations) {
		Stack<Abbreviation> tagStack = new Stack<>();

		while (data.hasRemaining()) {
			long tagOffset = data.position();
			long code = data.getUDATA();

			if (code != 0) {
				Abbreviation entry = abbreviations.apply(code);

				if (entry != null) {
					requestor.beginTag(entry.tag, tagOffset, entry.hasChildren);

					entry.readAttributes(requestor, data);

					if (entry.hasChildren) {
						tagStack.push(entry);
					} else {
						requestor.endTag(entry.tag, entry.hasChildren);
					}
				}
			} else {
				if (!tagStack.isEmpty()) {
					Abbreviation entry = tagStack.pop();

					requestor.endTag(entry.tag, entry.hasChildren);
				}
			}
		}

		while (!tagStack.isEmpty()) {
			Abbreviation entry = tagStack.pop();

			requestor.endTag(entry.tag, entry.hasChildren);
		}
	}

	private final DataSource abbrevSection;

	private final DataSource infoSection;

	private final LongFunction<String> stringAccessor;

	public DwarfScanner(String fileName) throws IOException {
		super();

		ByteBuffer empty = ByteBuffer.allocate(0);
		ByteBuffer abbrev = empty;
		ByteBuffer info = empty;
		ByteBuffer strings = empty;

		Elf object = new Elf(fileName);

		try {
			Elf.ELFhdr header = object.getELFhdr();
			ByteOrder order;

			if (header.e_ident[Elf.ELFhdr.EI_DATA] == Elf.ELFhdr.ELFDATA2LSB) {
				order = ByteOrder.LITTLE_ENDIAN;
			} else {
				order = ByteOrder.BIG_ENDIAN;
			}

			for (Elf.Section section : object.getSections()) {
				String name = section.toString();

				switch (name) {
				case ".debug_abbrev":
					abbrev = section.mapSectionData().order(order);
					break;
				case ".debug_info":
					info = section.mapSectionData().order(order);
					break;
				case ".debug_str":
					strings = section.mapSectionData().order(order);
					break;
				default:
					break;
				}
			}
		} finally {
			object.dispose();
		}

		Map<Long, String> stringCache = new HashMap<>();
		DataSource stringData = new DataSource(strings);
		Function<Long, String> stringReader = offset -> stringData.duplicate().position(offset.longValue()).getString();

		this.abbrevSection = new DataSource(abbrev);
		this.infoSection = new DataSource(info);
		this.stringAccessor = offset -> stringCache.computeIfAbsent(Long.valueOf(offset), stringReader);
	}

	public void scanUnits(DwarfRequestor requestor) {
		DataSource data = infoSection.duplicate();

		while (data.hasRemaining()) {
			DataSource unit = data.duplicate();
			long unitOffset = unit.position();
			long unitLength = unit.getU4();
			int offsetSize = 4;
			long abbrevOffset = 0;

			if (unitLength == 0 || unitLength == 0xFFFFFFFFL) {
				unitLength = unit.getU8();
				offsetSize = 8;
			}

			long nextUnit = unit.position() + unitLength;

			unit.limit(nextUnit);
			data.position(nextUnit);

			int version = unit.getU2();

			if (version < VERSION_MINIMUM || version > VERSION_MAXIMUM) {
				throw new IllegalArgumentException("version=" + version);
			}

			if (offsetSize == 8) {
				abbrevOffset = unit.getU8();
			} else {
				abbrevOffset = unit.getU4();
			}

			int addressSize = unit.getU1();

			requestor.enterCompilationUnit(unitOffset);

			DataSource abbrevs = abbrevSection.duplicate().position(abbrevOffset);
			LongFunction<Abbreviation> abbreviations = Abbreviation.readFrom(abbrevs);
			DataSource source = new DataSource(unit, addressSize, offsetSize, stringAccessor);

			scanTags(requestor, source, abbreviations);

			requestor.exitCompilationUnit(unitOffset);
		}
	}

}
