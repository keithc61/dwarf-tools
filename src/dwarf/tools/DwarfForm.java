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
 * Dwarf form codes.
 *
 * Source: http://www.dwarfstd.org/doc/DWARF4.pdf
 */
public interface DwarfForm {

	int DW_FORM_addr = 0x01;
	int DW_FORM_block2 = 0x03;
	int DW_FORM_block4 = 0x04;
	int DW_FORM_data2 = 0x05;
	int DW_FORM_data4 = 0x06;
	int DW_FORM_data8 = 0x07;
	int DW_FORM_string = 0x08;
	int DW_FORM_block = 0x09;
	int DW_FORM_block1 = 0x0a;
	int DW_FORM_data1 = 0x0b;
	int DW_FORM_flag = 0x0c;
	int DW_FORM_sdata = 0x0d;
	int DW_FORM_strp = 0x0e;
	int DW_FORM_udata = 0x0f;
	int DW_FORM_ref_addr = 0x10;
	int DW_FORM_ref1 = 0x11;
	int DW_FORM_ref2 = 0x12;
	int DW_FORM_ref4 = 0x13;
	int DW_FORM_ref8 = 0x14;
	int DW_FORM_ref_udata = 0x15;
	int DW_FORM_indirect = 0x16;
	int DW_FORM_sec_offset = 0x17;
	int DW_FORM_exprloc = 0x18;
	int DW_FORM_flag_present = 0x19;
	int DW_FORM_ref_sig8 = 0x20;

}
