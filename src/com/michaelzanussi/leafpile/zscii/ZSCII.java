package com.michaelzanussi.leafpile.zscii;

import java.util.ArrayList;
import java.util.List;

import com.michaelzanussi.leafpile.Debug;
import com.michaelzanussi.leafpile.zmachine.Memory;

/**
 * This interface defines the ZSCII character set and encoding
 * and decoding utilities. Z-machine text is a sequence of ZSCII
 * character codes. These ZSCII values are encoded in memory using
 * a string of Z-characters. (3.1)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (29 April 2016) 
 */
public abstract class ZSCII {
	
	protected Memory memory;
	protected Debug debug;
	
	protected int version;
	
	protected String a0 = " ~~~~~abcdefghijklmnopqrstuvwxyz";
	protected String a1 = " ~~~~~ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	protected String a2 = " ~~~~~ \n0123456789.,!?_#'\"/\\-:()";
	
	public ZSCII(Memory memory) {
		this.memory = memory;
		version = memory.getVersion();
	}
	
	/**
	 * Decode the string stored at address. Bit 7 is set
	 * only on the last 2-byte word of the text, and so
	 * marks the end. (3.2)
	 * 
	 * @param address starting address of string
	 * @return the decoded string
	 */
	public String decode(int address) {
		
		// Init some variables.
		List<Integer> array = new ArrayList<Integer>();
		int data = 0;
		
		// Read words until stop bit is set
		do {
			data = memory.getWord(address);
			address += 2;
			array.add(data);
		} while ((data & 0x8000) != 0x8000);
		
		// Convert ArrayList to primitive int array.
		int[] string_as_ints = new int[array.size()];
		int index = 0;
		for (int x : array) {
			string_as_ints[index++] = x;
		}
		
		// Return the decoded int array, a String.
		return decode(string_as_ints);
		
	}
	
	/**
	 * Convert the array of 2-byte words into an ASCII string.
	 * Defer implementation to the subclass.
	 * 
	 * @param data
	 * @return
	 */
	public abstract String decode(int[] data);
	
	/**
	 * Text in memory consists of a sequence of 2-byte words. Each word
	 * is divided into three 5-bit 'Z-characters', plus 1 bit left over.
	 * That bit is set only on the last 2-byte word of the text, and so
	 * marks the end. (3.2)
	 * 
	 * Convert a chunk of memory (a series of 2-byte words stored in an
	 * integer array) to its corresponding Z-characters (stored in a
	 * byte array).
	 * 
	 * @param data a chunk of memory to convert to Z-characters
	 * @return a byte array containing Z-characters
	 */
	protected byte[] toZchars(int[] data) {
		
		// Each 2-byte word contains 3 5-bit Z-characters and a stop bit,
		// so create a byte array large enough to hold the Z-characters.
		byte[] zchars = new byte[data.length * 3];
		
		byte stopbit;	// bit 15
		byte first;		// bits 10-14
		byte second;	// bits 5-9
		byte third;		// bits 0-4
		
		// Cycle through each 2-byte word and break it up into its
		// constituent parts, then add to the byte array.
		int index = 0;
		for (int i = 0; i < data.length; i++) {
			stopbit = (byte)(data[i] >> 15);
			third = (byte)(data[i] & 0x1f);
			second = (byte)((data[i] >> 5) & 0x1f);
			first = (byte)((data[i] >> 10) & 0x1f);
			zchars[index++] = first;
			zchars[index++] = second;
			zchars[index++] = third;
			System.out.println(data[i] + " = stopbit: " + stopbit + ", first: " + first + ", second: " + second + ", third: " + third);
		}
		
		return zchars;
		
	}
	
	/**
	 * Text in memory consists of a sequence of 2-byte words. Each word
	 * is divided into three 5-bit 'Z-characters', plus 1 bit left over.
	 * That bit is set only on the last 2-byte word of the text, and so
	 * marks the end. (3.2)
	 * 
	 * Convert an array of Z-characters into corresponding 2-byte word
	 * equivalent, and store in an integer array.
	 * 
	 * @param data an array containing Z-characters
	 * @return a integer array containing 2-byte words
	 */
	protected int[] fromZchars(byte[] data) {
		
		int[] words = new int[data.length / 3];
		
		int counter = 0;
		int word = 0;
		
		for (int i = 1; i <= data.length; i++) {
			word |= data[i - 1];
			if (i % 3 == 0) {
				// last set of bytes? set stopbit
				if (i == data.length) {
					word |= 0x8000;
				}
				// store it off
				words[counter] = word;
				// reset word
				word = 0;
				counter++;
			} else {
				word = word << 5;
			}
		}
		
		return words;
		
	}
	
	/**
	 * When an interpreter is encrypting typed-in text to match against 
	 * dictionary words, the following restrictions apply. Text should be 
	 * converted to lower case (as a result A1 will not be needed unless 
	 * the game provides its own alphabet table). Abbreviations may not 
	 * be used. The pad character, if needed, must be 5. The total string 
	 * length must be 6 Z-characters (in Versions 1 to 3) or 9 (Versions 
	 * 4 and later): any multi-Z-character constructions should be left 
	 * incomplete (rather than omitted) if there's no room to finish them.
	 * (3.7)
	 * 
	 * @param text the text to convert into Z-character string
	 * @return the Z-character string
	 */
	public byte[] encode(String text) {
		
		// set Z-string length
		int zlen = (version <= 3 ? 6 : 9);
		byte[] zstr = new byte[zlen];
		
		text = text.toLowerCase();
		
		for (int i = 0; i < zlen; i++) {
			if (text == null || i >= text.length()) {
				zstr[i] = 5;
			} else {
				char ch = text.charAt(i);
				if (Character.isLetter(ch)) {
					byte value = (byte)((ch - 'a') + 6);
					zstr[i] = value;
				} else {
					// not a letter
					assert (Character.isLetter(ch)) : "handle punctuatioon in decode()";
				}
			}
		}
				
		return zstr;
		
	}
	
}
