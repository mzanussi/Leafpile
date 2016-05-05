package com.michaelzanussi.leafpile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (2 May 2016) 
 */
public class V1ZSCII extends ZSCII {

	public V1ZSCII(Memory memory) {
		super(memory);
		if (version == 1) {
			// Adjust alphabet A2.
			a2 = " ~~~~~ 0123456789.,!?_#'\"/\\<-:()";
		}
	}

	public String decode(int[] data) {
		// set the default alphabet
		String alphabet = a0;
		// 1. get the Z-characters
		byte[] zchars = toZchars(data);
		// 2. convert to ZSCII string.
		StringBuilder sb = new StringBuilder();
		String lastAlphabet = null;
		for (int i = 0; i < zchars.length; i++) {
			if (zchars[i] >= 1 && zchars[i] <= 5) {
				switch (zchars[i]) {
				case 1:
					if (version == 1) {
						sb.append("\n");
					} else {
						// A 1 indicates an abbreviation (or synonym)
						i += 1;
						// Check if at end of array. If so, stop.
						if (i >= zchars.length) {
							break;
						}
						// The abbreviation table base address.
						int abbreviation_table = memory.getAbbreviationTableBase();
						// Get the number of the entry in the abbreviation table.
						int table_entry = zchars[i];
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
					}
					break;
				case 2:
					if (alphabet == a0) {
						alphabet = a1;
						lastAlphabet = a0;
					} else if (alphabet == a1) {
						alphabet = a2;
						lastAlphabet = a1;
					} else {
						alphabet = a0; 
						lastAlphabet = a2;
					}
					break;
				case 3:
					if (alphabet == a0) {
						alphabet = a2;
						lastAlphabet = a0;
					} else if (alphabet == a1) {
						alphabet = a0;
						lastAlphabet = a1;
					} else {
						alphabet = a1; 
						lastAlphabet = a2;
					}
					break;
				case 4:
					if (alphabet == a0) {
						alphabet = a1;
					} else if (alphabet == a1) {
						alphabet = a2;
					} else {
						alphabet = a0; 
					}
					break;
				case 5:
					if (alphabet == a0) {
						alphabet = a2;
					} else if (alphabet == a1) {
						alphabet = a0;
					} else {
						alphabet = a1; 
					}
					break;
				}
			} else {
				if (zchars[i] == 6 && alphabet == a2) {
					// next 2 z-chars specify 10-bit ZSCII; top 5 bits, then bottom 5-bits
					assert(false) : "zchar is 6 and alphabet is A2. string so far: " + sb.toString();
				} else {
					char ch = alphabet.charAt(zchars[i]);
					sb.append(ch);
				}
				if (lastAlphabet != null) {
					alphabet = lastAlphabet;
				}
			}
			
		}
		
		return sb.toString();
	
	}
	
}
