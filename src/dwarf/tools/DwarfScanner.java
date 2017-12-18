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
import java.util.function.ToLongFunction;

import org.eclipse.cdt.utils.elf.Elf;

public class DwarfScanner {

	private static class Abbreviation {

		private static Abbreviation find(Abbreviation[] a, long code) {
			Abbreviation abbreviation = null;

			if (0 < code && code <= a.length) {
				abbreviation = a[(int) code - 1];

				if (abbreviation.code != code) {
					abbreviation = null;
				}
			}

			if (abbreviation == null) {
				// FIXME find matching code
			}

			return abbreviation;
		}

		static LongFunction<Abbreviation> readFrom(DataSource data) {
			if (data.hasRemaining()) {
				return readFrom2(data.duplicate());
			}

			Map<Long, Abbreviation> abbreviations = new HashMap<>();

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
						entry.addAttribute(name, form, data);
					} else {
						break;
					}
				}

				abbreviations.put(Long.valueOf(code), entry);
			}

			return code -> abbreviations.get(Long.valueOf(code));
		}

		static LongFunction<Abbreviation> readFrom2(DataSource data) {
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
						entry.addAttribute(name, form, data);
					} else {
						break;
					}
				}

				abbreviations.add(entry);
			}

			Abbreviation[] list = abbreviations.toArray(new Abbreviation[abbreviations.size()]);

			Arrays.sort(list, Comparator.comparingLong(a -> a.code));

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

		private void addAttribute(long attribute, long form, DataSource data) {
			if ((0 < attribute && attribute <= Integer.MAX_VALUE) && (0 < form && form <= Integer.MAX_VALUE)) {
				attributes.add(AttributeReader.create((int) attribute, (int) form, data));
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

			Address(int attribute) {
				super(attribute);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long address = data.getAddress();

				requestor.acceptAddress(attribute, address);
			}

		}

		private static final class AddressIndex extends AttributeReader {

			private final ToLongFunction<DataSource> indexAccessor;

			AddressIndex(int attribute, ToLongFunction<DataSource> indexAccessor) {
				super(attribute);
				this.indexAccessor = indexAccessor;
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long index = indexAccessor.applyAsLong(data);

				requestor.acceptAddressIndex(attribute, index);
			}

		}

		private static class Block extends AttributeReader {

			private final ToLongFunction<DataSource> lengthAccessor;

			Block(int attribute, ToLongFunction<DataSource> lengthAccessor) {
				super(attribute);
				this.lengthAccessor = lengthAccessor;
			}

			@Override
			final void read(DwarfRequestor requestor, DataSource data) {
				int length = checkUInt(lengthAccessor.applyAsLong(data));
				byte[] block = new byte[length];

				data.getBlock(block);

				requestor.acceptBlock(attribute, block);
			}

		}

		private static final class Constant extends AttributeReader {

			private final ToLongFunction<DataSource> valueAccessor;

			Constant(int attribute, ToLongFunction<DataSource> valueAccessor) {
				super(attribute);
				this.valueAccessor = valueAccessor;
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long value = valueAccessor.applyAsLong(data);

				requestor.acceptConstant(attribute, value);
			}

		}

		private static class Expression extends AttributeReader {

			private final ToLongFunction<DataSource> lengthAccessor;

			Expression(int attribute, ToLongFunction<DataSource> lengthAccessor) {
				super(attribute);
				this.lengthAccessor = lengthAccessor;
			}

			@Override
			final void read(DwarfRequestor requestor, DataSource data) {
				int length = checkUInt(lengthAccessor.applyAsLong(data));
				byte[] expression = new byte[length];

				data.getBlock(expression);

				requestor.acceptExpression(attribute, expression);
			}

		}

		private static ToLongFunction<DataSource> constantAccessor(long value) {
			return data -> value;
		}

		private static final class Flag extends AttributeReader {

			private final ToLongFunction<DataSource> flagAccessor;

			Flag(int attribute, ToLongFunction<DataSource> flagAccessor) {
				super(attribute);
				this.flagAccessor = flagAccessor;
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				boolean flag = flagAccessor.applyAsLong(data) != 0;

				requestor.acceptFlag(attribute, flag);
			}

		}

		private static final class Reference extends AttributeReader {

			private final ToLongFunction<DataSource> offsetAccessor;

			Reference(int attribute, ToLongFunction<DataSource> offsetAccessor) {
				super(attribute);
				this.offsetAccessor = offsetAccessor;
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long offset = offsetAccessor.applyAsLong(data);

				requestor.acceptReference(attribute, offset);
			}

		}

		private static final class Str extends AttributeReader {

			Str(int attribute) {
				super(attribute);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				String string = data.getString();

				requestor.acceptString(attribute, string);
			}

		}

		private static final class StrRef extends AttributeReader {

			StrRef(int attribute) {
				super(attribute);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				long offset = data.getOffset();
				String string = data.lookupString(offset);

				requestor.acceptString(attribute, string);
			}

		}

		private static final class Indirect extends AttributeReader {

			Indirect(int attribute) {
				super(attribute);
			}

			@Override
			void read(DwarfRequestor requestor, DataSource data) {
				int form = checkUInt(data.getUDATA());
				AttributeReader indirect = create(attribute, form, data);

				indirect.read(requestor, data);
			}

		}

		/**
		 * This class allows us to parse abbreviations using unknown forms.
		 * If the abbreviation is unused this poses no problem.
		 * If it is used, we won't know how to handle the data associated
		 * with the attribute and instead throw an exception.
		 */
		private static final class Unknown extends AttributeReader {

			private final int form;

			Unknown(int attribute, int form) {
				super(attribute);
				this.form = form;
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

		static AttributeReader create(int attribute, int form, DataSource data) {
			switch (form) {
			case DwarfForm.DW_FORM_addr:
				return new Address(attribute);

			case DwarfForm.DW_FORM_block:
				return new Block(attribute, DataSource::getUDATA);
			case DwarfForm.DW_FORM_block1:
				return new Block(attribute, DataSource::getU1);
			case DwarfForm.DW_FORM_block2:
				return new Block(attribute, DataSource::getU2);
			case DwarfForm.DW_FORM_block4:
				return new Block(attribute, DataSource::getU4);

			case DwarfForm.DW_FORM_flag:
				return new Flag(attribute, DataSource::getU1);
			case DwarfForm.DW_FORM_flag_present:
				return new Flag(attribute, constantAccessor(1));

			case DwarfForm.DW_FORM_data1:
				return new Constant(attribute, DataSource::getU1);
			case DwarfForm.DW_FORM_data2:
				return new Constant(attribute, DataSource::getU2);
			case DwarfForm.DW_FORM_data4:
				return new Constant(attribute, DataSource::getU4);
			case DwarfForm.DW_FORM_data8:
				return new Constant(attribute, DataSource::getU8);
			case DwarfForm.DW_FORM_sdata:
				return new Constant(attribute, DataSource::getSDATA);
			case DwarfForm.DW_FORM_udata:
				return new Constant(attribute, DataSource::getUDATA);

			case DwarfForm.DW_FORM_implicit_const:
				return new Constant(attribute, constantAccessor(data.getSDATA()));

			case DwarfForm.DW_FORM_string:
				return new Str(attribute);
			case DwarfForm.DW_FORM_strp:
				return new StrRef(attribute);

			case DwarfForm.DW_FORM_ref1:
				return new Reference(attribute, DataSource::getU1);
			case DwarfForm.DW_FORM_ref2:
				return new Reference(attribute, DataSource::getU2);
			case DwarfForm.DW_FORM_ref4:
				return new Reference(attribute, DataSource::getU4);
			case DwarfForm.DW_FORM_ref8:
				return new Reference(attribute, DataSource::getU8);
			case DwarfForm.DW_FORM_ref_udata:
				return new Reference(attribute, DataSource::getUDATA);

			case DwarfForm.DW_FORM_sec_offset:
				return new Reference(attribute, DataSource::getOffset);

			case DwarfForm.DW_FORM_addrx:
				return new AddressIndex(attribute, DataSource::getUDATA);
			case DwarfForm.DW_FORM_addrx1:
				return new AddressIndex(attribute, DataSource::getU1);
			case DwarfForm.DW_FORM_addrx2:
				return new AddressIndex(attribute, DataSource::getU2);
			case DwarfForm.DW_FORM_addrx3:
				return new AddressIndex(attribute, DataSource::getU3);
			case DwarfForm.DW_FORM_addrx4:
				return new AddressIndex(attribute, DataSource::getU4);

			case DwarfForm.DW_FORM_exprloc:
				return new Expression(attribute, DataSource::getUDATA);

			case DwarfForm.DW_FORM_indirect:
				return new Indirect(attribute);

			case DwarfForm.DW_FORM_strx:
			case DwarfForm.DW_FORM_strx1:
			case DwarfForm.DW_FORM_strx2:
			case DwarfForm.DW_FORM_strx3:
			case DwarfForm.DW_FORM_strx4:

			case DwarfForm.DW_FORM_ref_sig8:
			case DwarfForm.DW_FORM_ref_sup4:
			case DwarfForm.DW_FORM_ref_sup8:

			case DwarfForm.DW_FORM_strp_sup:
			case DwarfForm.DW_FORM_data16:
			case DwarfForm.DW_FORM_line_strp:
			case DwarfForm.DW_FORM_loclistx:
			case DwarfForm.DW_FORM_rnglistx:

			case DwarfForm.DW_FORM_ref_addr:
			default:
				return new Unknown(attribute, form);
			}
		}

		final int attribute;

		AttributeReader(int attribute) {
			super();
			this.attribute = attribute;
		}

		abstract void read(DwarfRequestor requestor, DataSource data);

	}

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

			if (version < 2 || version > 5) {
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
