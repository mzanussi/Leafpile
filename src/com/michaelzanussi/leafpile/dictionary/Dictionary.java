package com.michaelzanussi.leafpile.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zscii.ZSCII;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (4 May 2016) 
 */
public abstract class Dictionary {
	
	private List<Character> word_separators;
	protected int entry_length;
	protected int num_entries;
	
	protected int address;
	
	protected ZSCII zscii;
	protected Map<String, Integer> dictionary;
	
	public Dictionary(Memory memory) {

		// Dictionary base address
		address = memory.getDictionaryBase();
		
		// number of word separators
		int n = memory.getByte(address++);

		// list of keyboard input codes (word separators)
		word_separators = new ArrayList<Character>();
		for (int i = 0; i < n; i++) {
			char word_separator = (char)memory.getByte(address++);
			word_separators.add(word_separator);
		}
		
		// entry length
		entry_length = memory.getByte(address++);
		
		// number of entries
		num_entries = memory.getWord(address);
		address += 2;
		
	}
	
}
