package com.michaelzanussi.leafpile.dictionary;

import java.util.HashMap;

import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (4 May 2016) 
 */
public class V4Dictionary extends Dictionary {

	// Encoded text of word, contains 9 z-chars
	// over 6 bytes, or 3 words. (13.4)
	private static final int ENCODED_WORDS = 3;

	public V4Dictionary(Memory memory) {
		super(memory);
		
		dictionary = new HashMap<String, Integer>();
		
		zscii = new V3ZSCII(memory);
		
		// encoded text of word
		int[] encoded_word = new int[ENCODED_WORDS];
		
		// extract each word in dictionary and add to Map.
		for (int i = 0; i < num_entries; i++) {
			int entry_address = address;
			// build the encoded word
			int msb = memory.getByte(address++);
			int lsb = memory.getByte(address++);
			encoded_word[0] = (msb << 8) | lsb;
			msb = memory.getByte(address++);
			lsb = memory.getByte(address++);
			encoded_word[1] = (msb << 8) | lsb;
			msb = memory.getByte(address++);
			lsb = memory.getByte(address++);
			encoded_word[2] = (msb << 8) | lsb;
			// decode the encoded word
			String zstr = zscii.decode(encoded_word);
			// set next entry address
			address = entry_address + entry_length;
			// store word off
			Integer ba = dictionary.put(zstr, entry_address);
			if (ba != null) {
				// should not happen
				assert(false) : "duplicate dictionary entry found";
			}
		}
		
	}

}
