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

/**
 * Dwarf attribute values.
 *
 * Source: http://www.dwarfstd.org/doc/DWARF4.pdf
 */
public interface DwarfAttribute {

	int DW_AT_sibling = 0x01;
	int DW_AT_location = 0x02;
	int DW_AT_name = 0x03;
	int DW_AT_ordering = 0x09;
	int DW_AT_byte_size = 0x0b;
	int DW_AT_bit_offset = 0x0c;
	int DW_AT_bit_size = 0x0d;
	int DW_AT_stmt_list = 0x10;
	int DW_AT_low_pc = 0x11;
	int DW_AT_high_pc = 0x12;
	int DW_AT_language = 0x13;
	int DW_AT_discr = 0x15;
	int DW_AT_discr_value = 0x16;
	int DW_AT_visibility = 0x17;
	int DW_AT_import = 0x18;
	int DW_AT_string_length = 0x19;
	int DW_AT_common_reference = 0x1a;
	int DW_AT_comp_dir = 0x1b;
	int DW_AT_const_value = 0x1c;
	int DW_AT_containing_type = 0x1d;
	int DW_AT_default_value = 0x1e;
	int DW_AT_inline = 0x20;
	int DW_AT_is_optional = 0x21;
	int DW_AT_lower_bound = 0x22;
	int DW_AT_producer = 0x25;
	int DW_AT_prototyped = 0x27;
	int DW_AT_return_addr = 0x2a;
	int DW_AT_start_scope = 0x2c;
	int DW_AT_bit_stride = 0x2e;
	int DW_AT_upper_bound = 0x2f;
	int DW_AT_abstract_origin = 0x31;
	int DW_AT_accessibility = 0x32;
	int DW_AT_address_class = 0x33;
	int DW_AT_artificial = 0x34;
	int DW_AT_base_types = 0x35;
	int DW_AT_calling_convention = 0x36;
	int DW_AT_count = 0x37;
	int DW_AT_data_member_location = 0x38;
	int DW_AT_decl_column = 0x39;
	int DW_AT_decl_file = 0x3a;
	int DW_AT_decl_line = 0x3b;
	int DW_AT_declaration = 0x3c;
	int DW_AT_discr_list = 0x3d;
	int DW_AT_encoding = 0x3e;
	int DW_AT_external = 0x3f;
	int DW_AT_frame_base = 0x40;
	int DW_AT_friend = 0x41;
	int DW_AT_identifier_case = 0x42;
	int DW_AT_macro_info = 0x43;
	int DW_AT_namelist_item = 0x44;
	int DW_AT_priority = 0x45;
	int DW_AT_segment = 0x46;
	int DW_AT_specification = 0x47;
	int DW_AT_static_link = 0x48;
	int DW_AT_type = 0x49;
	int DW_AT_use_location = 0x4a;
	int DW_AT_variable_parameter = 0x4b;
	int DW_AT_virtuality = 0x4c;
	int DW_AT_vtable_elem_location = 0x4d;
	int DW_AT_allocated = 0x4e;
	int DW_AT_associated = 0x4f;
	int DW_AT_data_location = 0x50;
	int DW_AT_byte_stride = 0x51;
	int DW_AT_entry_pc = 0x52;
	int DW_AT_use_UTF8 = 0x53;
	int DW_AT_extension = 0x54;
	int DW_AT_ranges = 0x55;
	int DW_AT_trampoline = 0x56;
	int DW_AT_call_column = 0x57;
	int DW_AT_call_file = 0x58;
	int DW_AT_call_line = 0x59;
	int DW_AT_description = 0x5a;
	int DW_AT_binary_scale = 0x5b;
	int DW_AT_decimal_scale = 0x5c;
	int DW_AT_small = 0x5d;
	int DW_AT_decimal_sign = 0x5e;
	int DW_AT_digit_count = 0x5f;
	int DW_AT_picture_string = 0x60;
	int DW_AT_mutable = 0x61;
	int DW_AT_threads_scaled = 0x62;
	int DW_AT_explicit = 0x63;
	int DW_AT_object_pointer = 0x64;
	int DW_AT_endianity = 0x65;
	int DW_AT_elemental = 0x66;
	int DW_AT_pure = 0x67;
	int DW_AT_recursive = 0x68;
	int DW_AT_signature = 0x69;
	int DW_AT_main_subprogram = 0x6a;
	int DW_AT_data_bit_offset = 0x6b;
	int DW_AT_const_expr = 0x6c;
	int DW_AT_enum_class = 0x6d;
	int DW_AT_linkage_name = 0x6e;

	int DW_AT_lo_user = 0x2000;
	int DW_AT_hi_user = 0x3fff;

}
