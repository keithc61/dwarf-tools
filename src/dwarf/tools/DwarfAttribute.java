package dwarf.tools;

/**
 * Dwarf attribute values.
 *
 * Source: http://www.dwarfstd.org/doc/DWARF5.pdf
 */
public interface DwarfAttribute {

	int DW_AT_sibling = 0x01; // reference
	int DW_AT_location = 0x02; // block, loclistptr
	int DW_AT_name = 0x03; // string
	int DW_AT_ordering = 0x09; // constant
	int DW_AT_byte_size = 0x0b; // block, constant, reference
	int DW_AT_bit_offset = 0x0c; // block, constant, reference
	int DW_AT_bit_size = 0x0d; // block, constant, reference
	int DW_AT_stmt_list = 0x10; // lineptr
	int DW_AT_low_pc = 0x11; // address
	int DW_AT_high_pc = 0x12; // address
	int DW_AT_language = 0x13; // constant
	int DW_AT_discr = 0x15; // reference
	int DW_AT_discr_value = 0x16; // constant
	int DW_AT_visibility = 0x17; // constant
	int DW_AT_import = 0x18; // reference
	int DW_AT_string_length = 0x19; // block, loclistptr
	int DW_AT_common_reference = 0x1a; // reference
	int DW_AT_comp_dir = 0x1b; // string
	int DW_AT_const_value = 0x1c; // block, constant, string
	int DW_AT_containing_type = 0x1d; // reference
	int DW_AT_default_value = 0x1e; // reference
	int DW_AT_inline = 0x20; // constant
	int DW_AT_is_optional = 0x21; // flag
	int DW_AT_lower_bound = 0x22; // block, constant, reference
	int DW_AT_producer = 0x25; // string
	int DW_AT_prototyped = 0x27; // flag
	int DW_AT_return_addr = 0x2a; // block, loclistptr
	int DW_AT_start_scope = 0x2c; // constant
	int DW_AT_bit_stride = 0x2e; // constant
	int DW_AT_upper_bound = 0x2f; // block, constant, reference
	int DW_AT_abstract_origin = 0x31; // reference
	int DW_AT_accessibility = 0x32; // constant
	int DW_AT_address_class = 0x33; // constant
	int DW_AT_artificial = 0x34; // flag
	int DW_AT_base_types = 0x35; // reference
	int DW_AT_calling_convention = 0x36; // constant
	int DW_AT_count = 0x37; // block, constant, reference
	int DW_AT_data_member_location = 0x38; // block, constant, loclistptr
	int DW_AT_decl_column = 0x39; // constant
	int DW_AT_decl_file = 0x3a; // constant
	int DW_AT_decl_line = 0x3b; // constant
	int DW_AT_declaration = 0x3c; // flag
	int DW_AT_discr_list = 0x3d; // block
	int DW_AT_encoding = 0x3e; // constant
	int DW_AT_external = 0x3f; // flag
	int DW_AT_frame_base = 0x40; // block, loclistptr
	int DW_AT_friend = 0x41; // reference
	int DW_AT_identifier_case = 0x42; // constant
	int DW_AT_macro_info = 0x43; // macptr
	int DW_AT_namelist_item = 0x44; // block
	int DW_AT_priority = 0x45; // reference
	int DW_AT_segment = 0x46; // block, loclistptr
	int DW_AT_specification = 0x47; // reference
	int DW_AT_static_link = 0x48; // block, loclistptr
	int DW_AT_type = 0x49; // reference
	int DW_AT_use_location = 0x4a; // block, loclistptr
	int DW_AT_variable_parameter = 0x4b; // flag
	int DW_AT_virtuality = 0x4c; // constant
	int DW_AT_vtable_elem_location = 0x4d; // block, loclistptr
	int DW_AT_allocated = 0x4e; // block, constant, reference
	int DW_AT_associated = 0x4f; // block, constant, reference
	int DW_AT_data_location = 0x50; // block
	int DW_AT_byte_stride = 0x51; // block, constant, reference
	int DW_AT_entry_pc = 0x52; // address
	int DW_AT_use_UTF8 = 0x53; // flag
	int DW_AT_extension = 0x54; // reference
	int DW_AT_ranges = 0x55; // rangelistptr
	int DW_AT_trampoline = 0x56; // address, flag, reference, string
	int DW_AT_call_column = 0x57; // constant
	int DW_AT_call_file = 0x58; // constant
	int DW_AT_call_line = 0x59; // constant
	int DW_AT_description = 0x5a; // string
	int DW_AT_binary_scale = 0x5b; // constant
	int DW_AT_decimal_scale = 0x5c; // constant
	int DW_AT_small = 0x5d; // reference
	int DW_AT_decimal_sign = 0x5e; // constant
	int DW_AT_digit_count = 0x5f; // constant
	int DW_AT_picture_string = 0x60; // string
	int DW_AT_mutable = 0x61; // flag
	int DW_AT_threads_scaled = 0x62; // flag
	int DW_AT_explicit = 0x63; // flag
	int DW_AT_object_pointer = 0x64; // reference
	int DW_AT_endianity = 0x65; // constant
	int DW_AT_elemental = 0x66; // flag
	int DW_AT_pure = 0x67; // flag
	int DW_AT_recursive = 0x68; // flag

	int DW_AT_lo_user = 0x2000;
	int DW_AT_hi_user = 0x3fff;

}
