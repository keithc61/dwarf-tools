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

public class DwarfDumper {

	@SuppressWarnings("boxing")
	private static class Requestor implements DwarfRequestor {

		private static String attributeName(int attribute) {
			switch (attribute) {
			case DwarfAttribute.DW_AT_sibling:
				return "sibling";
			case DwarfAttribute.DW_AT_location:
				return "location";
			case DwarfAttribute.DW_AT_name:
				return "name";
			case DwarfAttribute.DW_AT_ordering:
				return "ordering";
			case DwarfAttribute.DW_AT_byte_size:
				return "byte_size";
			case DwarfAttribute.DW_AT_bit_offset:
				return "bit_offset";
			case DwarfAttribute.DW_AT_bit_size:
				return "bit_size";
			case DwarfAttribute.DW_AT_stmt_list:
				return "stmt_list";
			case DwarfAttribute.DW_AT_low_pc:
				return "low_pc";
			case DwarfAttribute.DW_AT_high_pc:
				return "high_pc";
			case DwarfAttribute.DW_AT_language:
				return "language";
			case DwarfAttribute.DW_AT_discr:
				return "discr";
			case DwarfAttribute.DW_AT_discr_value:
				return "discr_value";
			case DwarfAttribute.DW_AT_visibility:
				return "visibility";
			case DwarfAttribute.DW_AT_import:
				return "import";
			case DwarfAttribute.DW_AT_string_length:
				return "string_length";
			case DwarfAttribute.DW_AT_common_reference:
				return "common_reference";
			case DwarfAttribute.DW_AT_comp_dir:
				return "comp_dir";
			case DwarfAttribute.DW_AT_const_value:
				return "const_value";
			case DwarfAttribute.DW_AT_containing_type:
				return "containing_type";
			case DwarfAttribute.DW_AT_default_value:
				return "default_value";
			case DwarfAttribute.DW_AT_inline:
				return "inline";
			case DwarfAttribute.DW_AT_is_optional:
				return "is_optional";
			case DwarfAttribute.DW_AT_lower_bound:
				return "lower_bound";
			case DwarfAttribute.DW_AT_producer:
				return "producer";
			case DwarfAttribute.DW_AT_prototyped:
				return "prototyped";
			case DwarfAttribute.DW_AT_return_addr:
				return "return_addr";
			case DwarfAttribute.DW_AT_start_scope:
				return "start_scope";
			case DwarfAttribute.DW_AT_bit_stride:
				return "bit_stride";
			case DwarfAttribute.DW_AT_upper_bound:
				return "upper_bound";
			case DwarfAttribute.DW_AT_abstract_origin:
				return "abstract_origin";
			case DwarfAttribute.DW_AT_accessibility:
				return "accessibility";
			case DwarfAttribute.DW_AT_address_class:
				return "address_class";
			case DwarfAttribute.DW_AT_artificial:
				return "artificial";
			case DwarfAttribute.DW_AT_base_types:
				return "base_types";
			case DwarfAttribute.DW_AT_calling_convention:
				return "calling_convention";
			case DwarfAttribute.DW_AT_count:
				return "count";
			case DwarfAttribute.DW_AT_data_member_location:
				return "data_member_location";
			case DwarfAttribute.DW_AT_decl_column:
				return "decl_column";
			case DwarfAttribute.DW_AT_decl_file:
				return "decl_file";
			case DwarfAttribute.DW_AT_decl_line:
				return "decl_line";
			case DwarfAttribute.DW_AT_declaration:
				return "declaration";
			case DwarfAttribute.DW_AT_discr_list:
				return "discr_list";
			case DwarfAttribute.DW_AT_encoding:
				return "encoding";
			case DwarfAttribute.DW_AT_external:
				return "external";
			case DwarfAttribute.DW_AT_frame_base:
				return "frame_base";
			case DwarfAttribute.DW_AT_friend:
				return "friend";
			case DwarfAttribute.DW_AT_identifier_case:
				return "identifier_case";
			case DwarfAttribute.DW_AT_macro_info:
				return "macro_info";
			case DwarfAttribute.DW_AT_namelist_item:
				return "namelist_item";
			case DwarfAttribute.DW_AT_priority:
				return "priority";
			case DwarfAttribute.DW_AT_segment:
				return "segment";
			case DwarfAttribute.DW_AT_specification:
				return "specification";
			case DwarfAttribute.DW_AT_static_link:
				return "static_link";
			case DwarfAttribute.DW_AT_type:
				return "type";
			case DwarfAttribute.DW_AT_use_location:
				return "use_location";
			case DwarfAttribute.DW_AT_variable_parameter:
				return "variable_parameter";
			case DwarfAttribute.DW_AT_virtuality:
				return "virtuality";
			case DwarfAttribute.DW_AT_vtable_elem_location:
				return "vtable_elem_location";
			case DwarfAttribute.DW_AT_allocated:
				return "allocated";
			case DwarfAttribute.DW_AT_associated:
				return "associated";
			case DwarfAttribute.DW_AT_data_location:
				return "data_location";
			case DwarfAttribute.DW_AT_byte_stride:
				return "byte_stride";
			case DwarfAttribute.DW_AT_entry_pc:
				return "entry_pc";
			case DwarfAttribute.DW_AT_use_UTF8:
				return "use_UTF8";
			case DwarfAttribute.DW_AT_extension:
				return "extension";
			case DwarfAttribute.DW_AT_ranges:
				return "ranges";
			case DwarfAttribute.DW_AT_trampoline:
				return "trampoline";
			case DwarfAttribute.DW_AT_call_column:
				return "call_column";
			case DwarfAttribute.DW_AT_call_file:
				return "call_file";
			case DwarfAttribute.DW_AT_call_line:
				return "call_line";
			case DwarfAttribute.DW_AT_description:
				return "description";
			case DwarfAttribute.DW_AT_binary_scale:
				return "binary_scale";
			case DwarfAttribute.DW_AT_decimal_scale:
				return "decimal_scale";
			case DwarfAttribute.DW_AT_small:
				return "small";
			case DwarfAttribute.DW_AT_decimal_sign:
				return "decimal_sign";
			case DwarfAttribute.DW_AT_digit_count:
				return "digit_count";
			case DwarfAttribute.DW_AT_picture_string:
				return "picture_string";
			case DwarfAttribute.DW_AT_mutable:
				return "mutable";
			case DwarfAttribute.DW_AT_threads_scaled:
				return "threads_scaled";
			case DwarfAttribute.DW_AT_explicit:
				return "explicit";
			case DwarfAttribute.DW_AT_object_pointer:
				return "object_pointer";
			case DwarfAttribute.DW_AT_endianity:
				return "endianity";
			case DwarfAttribute.DW_AT_elemental:
				return "elemental";
			case DwarfAttribute.DW_AT_pure:
				return "pure";
			case DwarfAttribute.DW_AT_recursive:
				return "recursive";
			default:
				return Integer.toString(attribute);
			}
		}

		private static void beginAttribute(int attribute) {
			System.out.printf("  DW_AT_%-12s ", attributeName(attribute));
		}

		private static String tagName(int tag) {
			switch (tag) {
			case DwarfTag.DW_TAG_array_type:
				return "array_type";
			case DwarfTag.DW_TAG_class_type:
				return "class_type";
			case DwarfTag.DW_TAG_entry_point:
				return "entry_point";
			case DwarfTag.DW_TAG_enumeration_type:
				return "enumeration_type";
			case DwarfTag.DW_TAG_formal_parameter:
				return "formal_parameter";
			case DwarfTag.DW_TAG_imported_declaration:
				return "imported_declaration";
			case DwarfTag.DW_TAG_label:
				return "label";
			case DwarfTag.DW_TAG_lexical_block:
				return "lexical_block";
			case DwarfTag.DW_TAG_member:
				return "member";
			case DwarfTag.DW_TAG_pointer_type:
				return "pointer_type";
			case DwarfTag.DW_TAG_reference_type:
				return "reference_type";
			case DwarfTag.DW_TAG_compile_unit:
				return "compile_unit";
			case DwarfTag.DW_TAG_string_type:
				return "string_type";
			case DwarfTag.DW_TAG_structure_type:
				return "structure_type";
			case DwarfTag.DW_TAG_subroutine_type:
				return "subroutine_type";
			case DwarfTag.DW_TAG_typedef:
				return "typedef";
			case DwarfTag.DW_TAG_union_type:
				return "union_type";
			case DwarfTag.DW_TAG_unspecified_parameters:
				return "unspecified_parameters";
			case DwarfTag.DW_TAG_variant:
				return "variant";
			case DwarfTag.DW_TAG_common_block:
				return "common_block";
			case DwarfTag.DW_TAG_common_inclusion:
				return "common_inclusion";
			case DwarfTag.DW_TAG_inheritance:
				return "inheritance";
			case DwarfTag.DW_TAG_inlined_subroutine:
				return "inlined_subroutine";
			case DwarfTag.DW_TAG_module:
				return "module";
			case DwarfTag.DW_TAG_ptr_to_member_type:
				return "ptr_to_member_type";
			case DwarfTag.DW_TAG_set_type:
				return "set_type";
			case DwarfTag.DW_TAG_subrange_type:
				return "subrange_type";
			case DwarfTag.DW_TAG_with_stmt:
				return "with_stmt";
			case DwarfTag.DW_TAG_access_declaration:
				return "access_declaration";
			case DwarfTag.DW_TAG_base_type:
				return "base_type";
			case DwarfTag.DW_TAG_catch_block:
				return "catch_block";
			case DwarfTag.DW_TAG_const_type:
				return "const_type";
			case DwarfTag.DW_TAG_constant:
				return "constant";
			case DwarfTag.DW_TAG_enumerator:
				return "enumerator";
			case DwarfTag.DW_TAG_file_type:
				return "file_type";
			case DwarfTag.DW_TAG_friend:
				return "friend";
			case DwarfTag.DW_TAG_namelist:
				return "namelist";
			case DwarfTag.DW_TAG_namelist_item:
				return "namelist_item";
			case DwarfTag.DW_TAG_packed_type:
				return "packed_type";
			case DwarfTag.DW_TAG_subprogram:
				return "subprogram";
			case DwarfTag.DW_TAG_template_type_parameter:
				return "template_type_parameter";
			case DwarfTag.DW_TAG_template_value_parameter:
				return "template_value_parameter";
			case DwarfTag.DW_TAG_thrown_type:
				return "thrown_type";
			case DwarfTag.DW_TAG_try_block:
				return "try_block";
			case DwarfTag.DW_TAG_variant_part:
				return "variant_part";
			case DwarfTag.DW_TAG_variable:
				return "variable";
			case DwarfTag.DW_TAG_volatile_type:
				return "volatile_type";
			case DwarfTag.DW_TAG_dwarf_procedure:
				return "dwarf_procedure";
			case DwarfTag.DW_TAG_restrict_type:
				return "restrict_type";
			case DwarfTag.DW_TAG_interface_type:
				return "interface_type";
			case DwarfTag.DW_TAG_namespace:
				return "namespace";
			case DwarfTag.DW_TAG_imported_module:
				return "imported_module";
			case DwarfTag.DW_TAG_unspecified_type:
				return "unspecified_type";
			case DwarfTag.DW_TAG_partial_unit:
				return "partial_unit";
			case DwarfTag.DW_TAG_imported_unit:
				return "imported_unit";
			case DwarfTag.DW_TAG_condition:
				return "condition";
			case DwarfTag.DW_TAG_shared_type:
				return "shared_type";
			default:
				return Integer.toString(tag);
			}
		}

		private int tagDepth;

		Requestor() {
			super();
			this.tagDepth = 0;
		}

		@Override
		public void acceptAddress(int attribute, long address) {
			beginAttribute(attribute);
			System.out.printf("address 0x%x%n", address);
		}

		@Override
		public void acceptBlock(int attribute, byte[] data) {
			beginAttribute(attribute);
			System.out.printf("block length %d%n", data.length);
		}

		@Override
		public void acceptConstant(int attribute, long value) {
			beginAttribute(attribute);
			System.out.printf("const %d%n", value);
		}

		@Override
		public void acceptExpression(int attribute, byte[] expression) {
			beginAttribute(attribute);
			System.out.printf("expression length %d%n", expression.length);
		}

		@Override
		public void acceptFlag(int attribute, boolean flag) {
			beginAttribute(attribute);
			System.out.printf("flag %s%n", flag ? "Y" : "N");
		}

		@Override
		public void acceptReference(int attribute, long offset) {
			beginAttribute(attribute);
			System.out.printf("ref 0x%x%n", offset);
		}

		@Override
		public void acceptString(int attribute, String string) {
			beginAttribute(attribute);
			System.out.printf("string %s%n", string);
		}

		@Override
		public void beginTag(int tag, long offset, boolean hasChildren) {
			System.out.printf("<%x><%x> DW_TAG_%s children=%s%n", //
					tagDepth, offset, tagName(tag), hasChildren ? "Y" : "N");
			tagDepth += 1;
		}

		@Override
		public void endTag(int tag, boolean hasChildren) {
			tagDepth -= 1;
			System.out.printf("<%x><<<< DW_TAG_%s children=%s%n", //
					tagDepth, tagName(tag), hasChildren ? "Y" : "N");
		}

		@Override
		public void enterCompilationUnit(long offset) {
			return;
		}

		@Override
		public void exitCompilationUnit(long offset) {
			return;
		}

	}

	public static void main(String[] args) {
		DwarfRequestor requestor = new Requestor();

		for (String fileName : args) {
			try {
				DwarfScanner dumper = new DwarfScanner(fileName);

				dumper.scanUnits(requestor);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
