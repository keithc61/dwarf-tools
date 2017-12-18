package dwarf.tools;

import java.io.IOException;

public class DwarfScanTest {

	public static void main(String[] args) {
		DwarfRequestor requestor = DwarfRequestor.NULL;

		for (String fileName : args) {
			long start = System.nanoTime();

			try {
				DwarfScanner dumper = new DwarfScanner(fileName);

				dumper.scanUnits(requestor);
			} catch (IOException e) {
				e.printStackTrace();
			}

			long duration = System.nanoTime() - start;

			System.out.printf("Scanned %s in %.6f seconds%n", fileName, Double.valueOf(duration / 1e9));
		}
	}

}
