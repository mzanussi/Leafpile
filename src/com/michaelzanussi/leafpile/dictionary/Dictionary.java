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

		// Dictionary base address (13.1)
		address = memory.getDictionaryBase();
		
		// number of word separators (13.2)
		int n = memory.getByte(address++);

		// list of keyboard input codes (word separators) (13.2)
		word_separators = new ArrayList<Character>();
		for (int i = 0; i < n; i++) {
			char word_separator = (char)memory.getByte(address++);
			word_separators.add(word_separator);
		}
		
		// entry length (13.2)
		entry_length = memory.getByte(address++);
		
		// number of entries (13.2)
		num_entries = memory.getWord(address);
		address += 2;
		
	}
	
	/**
	 * Returns the address of the word in the dictionary.
	 * 
	 * @param word The word to locate in the dictionary.
	 * @return the address of the word in the dictionary.
	 */
	public int lookup(String word) {
		Integer addr = dictionary.get(word);
		return (addr != null ? addr.intValue() : 0);
	}

}
