package dwarf.tools;

import java.io.IOException;

public class DwarfScanTest {

	private static final class DwarfCounter implements DwarfRequestor {

		private int attributeCount;

		private int tagBeginCount;

		private int tagEndCount;

		private int unitBeginCount;

		private int unitEndCount;

		public DwarfCounter() {
			super();
			this.attributeCount = 0;
			this.tagBeginCount = 0;
			this.tagEndCount = 0;
			this.unitBeginCount = 0;
			this.unitEndCount = 0;
		}

		@Override
		public void acceptAddress(int attribute, long address) {
			attributeCount += 1;
		}

		@Override
		public void acceptBlock(int attribute, byte[] data) {
			attributeCount += 1;
		}

		@Override
		public void acceptConstant(int attribute, long value) {
			attributeCount += 1;
		}

		@Override
		public void acceptExpression(int attribute, byte[] expression) {
			attributeCount += 1;
		}

		@Override
		public void acceptFlag(int attribute, boolean flag) {
			attributeCount += 1;
		}

		@Override
		public void acceptReference(int attribute, long offset) {
			attributeCount += 1;
		}

		@Override
		public void acceptString(int attribute, String string) {
			attributeCount += 1;
		}

		@Override
		public void beginTag(int tag, long offset, boolean hasChildren) {
			tagBeginCount += 1;
		}

		@Override
		public void endTag(int tag, boolean hasChildren) {
			tagEndCount += 1;
		}

		@Override
		public void enterCompilationUnit(long offset) {
			unitBeginCount += 1;
		}

		@Override
		public void exitCompilationUnit(long offset) {
			unitEndCount += 1;
		}

		@SuppressWarnings("boxing")
		void printStatistics() {
			System.out.printf("  %d compilation units", unitBeginCount);
			if (unitEndCount != unitBeginCount) {
				System.out.printf(" (exited %d)", unitEndCount);
			}
			System.out.println();
			System.out.printf("  %d tags", tagBeginCount);
			if (tagEndCount != tagBeginCount) {
				System.out.printf(" (exited %d)", tagEndCount);
			}
			System.out.println();
			System.out.printf("  %d attributes%n", attributeCount);
		}

	}

	public static void main(String[] args) {
		for (String fileName : args) {
			DwarfCounter counter = new DwarfCounter();
			long start = System.nanoTime();

			try {
				DwarfScanner dumper = new DwarfScanner(fileName);

				dumper.scanUnits(counter);
			} catch (IOException e) {
				e.printStackTrace();
			}

			long duration = System.nanoTime() - start;

			System.out.printf("Scanned %s in %.6f seconds%n", fileName, Double.valueOf(duration / 1e9));
			counter.printStatistics();
		}
	}

}
