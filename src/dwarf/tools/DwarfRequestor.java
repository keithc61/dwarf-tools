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

public interface DwarfRequestor {

	/**
	 * Enter a compilation unit.
	 * 
	 * @param offset section offset of the compilation unit
	 */
	void enterCompilationUnit(long offset);

	/**
	 * Exit the current compilation unit.
	 * 
	 * @param offset section offset of the compilation unit
	 */
	void exitCompilationUnit(long offset);

	void beginTag(int tag, long offset, boolean hasChildren);

	void endTag(int tag, boolean hasChildren);

	void acceptAddress(int attribute, long address);

	void acceptBlock(int attribute, byte[] data);

	void acceptConstant(int attribute, long value);

	void acceptFlag(int attribute, boolean flag);

	void acceptReference(int attribute, long offset);

	void acceptString(int attribute, String string);

	void acceptExpression(int attribute, byte[] expression);

	DwarfRequestor NULL = new DwarfRequestor() {

		@Override
		public void acceptAddress(int attribute, long address) {
			return;
		}

		@Override
		public void enterCompilationUnit(long offset) {
			return;
		}

		@Override
		public void exitCompilationUnit(long offset) {
			return;
		}

		@Override
		public void beginTag(int tag, long offset, boolean hasChildren) {
			return;
		}

		@Override
		public void endTag(int tag, boolean hasChildren) {
			return;
		}

		@Override
		public void acceptBlock(int attribute, byte[] data) {
			return;
		}

		@Override
		public void acceptConstant(int attribute, long value) {
			return;
		}

		@Override
		public void acceptFlag(int attribute, boolean flag) {
			return;
		}

		@Override
		public void acceptReference(int attribute, long offset) {
			return;
		}

		@Override
		public void acceptString(int attribute, String string) {
			return;
		}

		@Override
		public void acceptExpression(int attribute, byte[] expression) {
			return;
		}

	};

}
