package com.michaelzanussi.leafpile.zscii;

import java.util.ArrayList;
import java.util.List;

import com.michaelzanussi.leafpile.zmachine.Memory;

/**
 * Support for V3-V4 alphabet: shift, abbreviations.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (29 April 2016) 
 */
public class V3ZSCII extends ZSCII {
	
	public V3ZSCII(Memory memory) {
		super(memory);
		// TODO: look at $34 in header. see 3.5.5 and 3.5.5.1 for more info.
		assert (version < 5) : "add support for alt alphabets";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.zscii.ZSCII#decode(int)
	 */
/*	public String decode(int address) {
		int text_length = memory.getByte(address++);
		assert(text_length>0) : "hmm text length is 0. troubleshoot.";
		int[] short_name_ints = new int[text_length];
		for (int i = 0; i < text_length; i++) {
			short_name_ints[i] = memory.getWord(address + (i * 2));
		}
		return decode(short_name_ints);
	}*/

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.zscii.ZSCII#decode(int[])
	 */
	@Override
	public String decode(int[] data) {
		
		// set the default alphabet
		String alphabet = a0;
		// 1. get the Z-characters
		byte[] zchars = toZchars(data);
		// 2. convert to ZSCII string.
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < zchars.length; i++) {
			if (zchars[i] >= 1 && zchars[i] <= 5) {
				switch (zchars[i]) {
				case 1:
				case 2:
				case 3:
					// A 1, 2 or 3 indicates an abbreviation (or synonym)
					int synonym = zchars[i];
					i += 1;
					// Check if at end of array. If so, stop.
					if (i >= zchars.length) {
						break;
					}
					// The abbreviation table base address.
					int abbreviation_table = memory.getAbbreviationTableBase();
					// Get the number of the entry in the abbreviation table.
					int table_entry = (32 * (synonym - 1)) + zchars[i];
					// Calculate the address of the abbreviation.
					int address = memory.getWord(abbreviation_table + table_entry * 2) * 2;
					// Pull all the words that make up the abbreviation.
					int stopbit = 0;
					List<Integer> abbr_array = new ArrayList<Integer>();
					do {
						int word = memory.getWord(address);
						abbr_array.add(word);
						stopbit = word >> 15;
						address += 2;
					} while (stopbit == 0);
					// Need this to be in primitive format.
					int[] prim_abbr_array = new int[abbr_array.size()];
					for (int j = 0; j < abbr_array.size(); j++) {
						prim_abbr_array[j] = abbr_array.get(j);
					}
					// Finally decode the abbreviation.
					sb.append(decode(prim_abbr_array));
					break;
				case 4:
					alphabet = a1;
					break;
				case 5:
					alphabet = a2;
					break;
				}
			} else {
				if (zchars[i] == 6 && alphabet == a2) {
					// next 2 z-chars specify 10-bit ZSCII; top 5 bits, then bottom 5-bits (3.4)
					byte a = zchars[++i];
					byte b = zchars[++i];
					// Shift-left first 5 bits of first z-char and 
					// bitwise OR with first 5 bits of second z-char,
					// giving us a 10-bit ZSCII character code.
					int nchar = ((a & 0x1f) << 5) | (b & 0x1f);
					//char ch = alphabet.charAt(nchar);
					sb.append(String.valueOf((char)nchar));
					alphabet = a1;
					//assert(false) : "zchar is 6 and alphabet is A2. string so far: " + sb.toString();
				} else {
					char ch = alphabet.charAt(zchars[i]);
					sb.append(ch);
				}
				alphabet = a0;
			}
			
		}
		
		return sb.toString();
		
	}

}
