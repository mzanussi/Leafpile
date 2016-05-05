package com.michaelzanussi.leafpile;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Z-machine memory (i.e. the story)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (27 April 2016) 
 */
public class Memory {
	
	private byte[] memory;
	
	private Debug debug;
	
	private static final int VERSION = 0x00;
	private static final int FLAGS1 = 0x01;				// TODO:
	private static final int HIGH_MEMORY = 0x04;
	private static final int PC = 0x06;
	private static final int DICTIONARY = 0x08;
	private static final int OBJECT_TABLE = 0x0a;
	private static final int GLOBALS_TABLE = 0x0c;
	private static final int STATIC_MEMORY = 0x0e;
	private static final int FLAGS2 = 0x10;				// TODO:
	private static final int ABBREVIATION_TABLE = 0x18;
	private static final int FILE_LENGTH = 0x1a;
	private static final int CHECKSUM = 0x1c;
	private static final int TERP_NUMBER = 0x1e;
	private static final int TERP_VERSION = 0x1f;
	private static final int SCREEN_HEIGHT = 0x20;
	private static final int SCREEN_WIDTH = 0x21;
	private static final int SCREEN_WIDTH_UNITS = 0x22;
	private static final int SCREEN_HEIGHT_UNITS = 0x24;
	private static final int FONT_WIDTH = 0x26;
	private static final int FONT_HEIGHT = 0x27;
	private static final int ROUTINES_OFFSET = 0x28;
	private static final int STATIC_STRINGS_OFFSET = 0x2a;
	
	public Memory(File file) {
		
		debug = new Debug(true);	// TODO: get from property file instead
		
		if (file == null) {
			throw new NullPointerException("No story file specified.");
		}
		
		try {
			int len = (int)file.length();
			memory = new byte[len];
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			in.readFully(memory);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Debug getDebug() {
		// TODO: this needs to more out of this class into
		// the main GUI class or whatever.
		return debug;
	}
	
	/**
	 * Get the byte located at address.
	 * 
	 * @param address the address in memory
	 * @return the byte located at address
	 */
	public int getByte(int address) {
		if (address < 0 || address > memory.length) {
			throw new IndexOutOfBoundsException("Invalid address: " + address);
		}
		int value = memory[address] & 0xFF;
		{ debug.println("--> byte [" + address + "] = " + value); }
		return value;
	}
	
	/**
	 * Set the byte located at address to the passed value.
	 * 
	 * @param address the address in memory
	 * @param value the new value
	 */
	public void setByte(int address, int value) {
		if (address < 0 || address > memory.length) {
			throw new IndexOutOfBoundsException("Invalid address: " + address);
		}
		byte byte_value = (byte)(value & 0xff);
		memory[address] = byte_value;
	}
	
	/**
	 * Get the word beginning at address. Big endian.
	 * 
	 * @param address the beginning address in memory
	 * @return the word beginning at address 
	 */
	public int getWord(int address) {
		if (address < 0 || address > memory.length - 1) {
			throw new IndexOutOfBoundsException("Invalid address: " + address);
		}
		int value = ((memory[address] & 0xff) << 8) | (memory[address + 1] & 0xff);
		{ debug.println("--> word [" + address + "][" + (address+1) + "] = " + value); }
		return value;
	}
	
	/**
	 * Set the word beginning at address. Big Endian.
	 * 
	 * @param address the beginning address in memory
	 * @param value the new value
	 */
	public void setWord(int address, int value) {
		if (address < 0 || address > memory.length - 1) {
			throw new IndexOutOfBoundsException("Invalid address: " + address);
		}
		byte msb = (byte)((value >> 8) & 0xff);
		byte lsb = (byte)(value & 0xff);
		memory[address] = msb;
		memory[address + 1] = lsb;
	}
	
	/**
	 * Returns the story file version.
	 * 
	 * @return the story file version
	 */
	public int getVersion() {
		//{ debug.print("story ver: "); }
		return getByte(VERSION);
	}
	
	/**
	 * Returns the base of high memory.
	 * 
	 * @return the base of high memory
	 */
	public int getHighMemoryBase() {
		return getWord(HIGH_MEMORY);
	}
	
	/**
	 * Returns the initial value of program counter.
	 * TODO: ver6> packed addr of 1st routine
	 * 
	 * @return the initial value of program counter
	 */
	public int getInitialPC() {
		assert (getVersion() < 6) : "complete implementation for getInitialPC()";
		return getWord(PC);
	}
	
	/**
	 * Returns the location of the dictionary.
	 * 
	 * @return the location of the dictionary
	 */
	public int getDictionaryBase() {
		return getWord(DICTIONARY);
	}
	
	/**
	 * Returns the location of the object table.
	 * 
	 * @return the location of the object table
	 */
	public int getObjectTableBase() {
		//System.out.print("obj tbl base: ");
		return getWord(OBJECT_TABLE);
	}
	
	/**
	 * Returns the location of the global variables table.
	 * 
	 * @return the location of the global variables table
	 */
	public int getGlobalVariablesTableBase() {
		return getWord(GLOBALS_TABLE);
	}
	
	/**
	 * Returns the base of static memory.
	 * 
	 * @return the base of static memory
	 */
	public int getStaticMemoryBase() {
		return getWord(STATIC_MEMORY);
	}
	
	/**
	 * Returns the base of abbreviations table.
	 * 
	 * @return the base of abbreviations table.
	 */
	public int getAbbreviationTableBase() {
		return getWord(ABBREVIATION_TABLE);
	}
	
	/**
	 * Returns the file length of the story.
	 * 
	 * @return the file length of the story.
	 */
	public int getFileLength() {
		int l = getWord(FILE_LENGTH);
		if (getVersion() <= 3) {
			l *= 2;
		} else if (getVersion() <= 5) {
			l *= 4;
		} else {
			l *= 8;
		}
		return l;
	}
	
	public int getChecksum() {
		return getWord(CHECKSUM);
	}
	
	public int getInterpreterNumber() {
		return getByte(TERP_NUMBER);
	}
	
	public int getInterpreterVersion() {
		return getByte(TERP_VERSION);
	}
	
	public int getScreenHeight() {
		return getByte(SCREEN_HEIGHT);
	}
	
	public int getScreenHeightInUnits() {
		return getWord(SCREEN_HEIGHT_UNITS);
	}
	
	public int getScreenWidth() {
		return getByte(SCREEN_WIDTH);
	}
	
	public int getScreenWidthInUnits() {
		return getWord(SCREEN_WIDTH_UNITS);
	}
	
	public int getFontWidth() {
		if (getVersion() == 5) {
			return getByte(FONT_WIDTH);
		} else {
			return getByte(FONT_HEIGHT);
		}
	}
	
	public int getFontHeight() {
		if (getVersion() == 5) {
			return getByte(FONT_HEIGHT);
		} else {
			return getByte(FONT_WIDTH);
		}
	}
	
	public int getRoutinesOffset() {
		return getWord(ROUTINES_OFFSET) * 8;
	}
	
	public int getStaticStringsOffset() {
		return getWord(STATIC_STRINGS_OFFSET) * 8;
	}
	
}
