package dwarf.tools;

/**
 * Dwarf tag values.
 *
 * Source: http://www.dwarfstd.org/doc/DWARF4.pdf
 */
public interface DwarfTag {

	int DW_TAG_array_type = 0x01;
	int DW_TAG_class_type = 0x02;
	int DW_TAG_entry_point = 0x03;
	int DW_TAG_enumeration_type = 0x04;
	int DW_TAG_formal_parameter = 0x05;
	int DW_TAG_imported_declaration = 0x08;
	int DW_TAG_label = 0x0a;
	int DW_TAG_lexical_block = 0x0b;
	int DW_TAG_member = 0x0d;
	int DW_TAG_pointer_type = 0x0f;
	int DW_TAG_reference_type = 0x10;
	int DW_TAG_compile_unit = 0x11;
	int DW_TAG_string_type = 0x12;
	int DW_TAG_structure_type = 0x13;
	int DW_TAG_subroutine_type = 0x15;
	int DW_TAG_typedef = 0x16;
	int DW_TAG_union_type = 0x17;
	int DW_TAG_unspecified_parameters = 0x18;
	int DW_TAG_variant = 0x19;
	int DW_TAG_common_block = 0x1a;
	int DW_TAG_common_inclusion = 0x1b;
	int DW_TAG_inheritance = 0x1c;
	int DW_TAG_inlined_subroutine = 0x1d;
	int DW_TAG_module = 0x1e;
	int DW_TAG_ptr_to_member_type = 0x1f;
	int DW_TAG_set_type = 0x20;
	int DW_TAG_subrange_type = 0x21;
	int DW_TAG_with_stmt = 0x22;
	int DW_TAG_access_declaration = 0x23;
	int DW_TAG_base_type = 0x24;
	int DW_TAG_catch_block = 0x25;
	int DW_TAG_const_type = 0x26;
	int DW_TAG_constant = 0x27;
	int DW_TAG_enumerator = 0x28;
	int DW_TAG_file_type = 0x29;
	int DW_TAG_friend = 0x2a;
	int DW_TAG_namelist = 0x2b;
	int DW_TAG_namelist_item = 0x2c;
	int DW_TAG_packed_type = 0x2d;
	int DW_TAG_subprogram = 0x2e;
	int DW_TAG_template_type_parameter = 0x2f;
	int DW_TAG_template_value_parameter = 0x30;
	int DW_TAG_thrown_type = 0x31;
	int DW_TAG_try_block = 0x32;
	int DW_TAG_variant_part = 0x33;
	int DW_TAG_variable = 0x34;
	int DW_TAG_volatile_type = 0x35;
	int DW_TAG_dwarf_procedure = 0x36;
	int DW_TAG_restrict_type = 0x37;
	int DW_TAG_interface_type = 0x38;
	int DW_TAG_namespace = 0x39;
	int DW_TAG_imported_module = 0x3a;
	int DW_TAG_unspecified_type = 0x3b;
	int DW_TAG_partial_unit = 0x3c;
	int DW_TAG_imported_unit = 0x3d;
	int DW_TAG_condition = 0x3f;
	int DW_TAG_shared_type = 0x40;
	int DW_TAG_type_unit = 0x41;
	int DW_TAG_rvalue_reference_type = 0x42;
	int DW_TAG_template_alias = 0x43;

	int DW_TAG_lo_user = 0x4080;
	int DW_TAG_hi_user = 0xffff;

}
