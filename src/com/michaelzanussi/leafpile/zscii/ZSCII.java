package com.michaelzanussi.leafpile.zscii;

import com.michaelzanussi.leafpile.Debug;
import com.michaelzanussi.leafpile.zmachine.Memory;

/**
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
	
	public abstract String decode(int[] data);
	
	// When an interpreter is encrypting typed-in text to match against dictionary words, 
	// the following restrictions apply. Text should be converted to lower case (as a 
	// result A1 will not be needed unless the game provides its own alphabet table). 
	// Abbreviations may not be used. The pad character, if needed, must be 5. The total 
	// string length must be 6 Z-characters (in Versions 1 to 3) or 9 (Versions 4 and later): 
	// any multi-Z-character constructions should be left incomplete (rather than omitted) 
	// if there's no room to finish them.
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
		
		//int[] words = fromZchars(zstr);
		
		return zstr;
		
	}
	
}
