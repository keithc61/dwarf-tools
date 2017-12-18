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

	void acceptAddressIndex(int attribute, long index);

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
		public void acceptAddressIndex(int attribute, long index) {
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
