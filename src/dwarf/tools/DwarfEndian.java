package dwarf.tools;

/**
 * Dwarf endian values.
 *
 * Source: http://www.dwarfstd.org/doc/DWARF5.pdf
 */
public interface DwarfEndian {

	int DW_END_default = 0x00;
	int DW_END_big = 0x01;
	int DW_END_little = 0x02;

	int DW_END_lo_user = 0x40;
	int DW_END_hi_user = 0xff;

}
