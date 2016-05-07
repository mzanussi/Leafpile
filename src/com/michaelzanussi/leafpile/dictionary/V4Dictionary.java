package com.michaelzanussi.leafpile.dictionary;

import java.util.HashMap;

import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (4 May 2016) 
 */
public class V4Dictionary extends Dictionary {

	public V4Dictionary(Memory memory) {
		super(memory);
		
		dictionary = new HashMap<String, Integer>();
		
		zscii = new V3ZSCII(memory);
		
		// encoded text of word
		int[] encoded_word = new int[3];
		
		int msb, lsb;
		
		for (int i = 0; i < num_entries; i++) {
			int base_address = address;
			msb = memory.getByte(address++);
			lsb = memory.getByte(address++);
			encoded_word[0] = (msb << 8) | lsb;
			msb = memory.getByte(address++);
			lsb = memory.getByte(address++);
			encoded_word[1] = (msb << 8) | lsb;
			msb = memory.getByte(address++);
			lsb = memory.getByte(address++);
			encoded_word[2] = (msb << 8) | lsb;
			String zstr = zscii.decode(encoded_word);
			// SKIP bytes of data (entry_length-4 bytes)
			int data_len = entry_length - 4;
			address += data_len; 
			// store word it off
			Integer ba = dictionary.put(zstr, base_address);
			if (ba != null) {
				// should not happen
				assert(ba==null) : "duplicate dictionary entry found";
			}
		}
		
	}

}
