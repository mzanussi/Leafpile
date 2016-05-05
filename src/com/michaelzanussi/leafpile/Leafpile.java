package com.michaelzanussi.leafpile;

import java.io.File;

import com.michaelzanussi.leafpile.ui.LeafpileGUI;

/**
 * The Leafpile GUI.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (15 February 2008) 
 */
public class Leafpile {
	
	public void cli() {
		// CLI tests
		File file = new File("test/tdata.z3");
		Memory memory = new Memory(file);
		System.out.println("Version: " + memory.getVersion());
		System.out.println("High memory base: " + memory.getHighMemoryBase());
		System.out.println("Initial PC: " + memory.getInitialPC());
		System.out.println("Location of dictionary: " + memory.getDictionaryBase());
		System.out.println("Location of object table base: " + memory.getObjectTableBase());
		System.out.println("Location of global variables table base: " + memory.getGlobalVariablesTableBase());
		System.out.println("Static memory base: " + memory.getStaticMemoryBase());
		System.out.println("Abbreviations table base: " + memory.getAbbreviationTableBase());
		System.out.println("File length: " + memory.getFileLength());
		System.out.println("File checksum: " + memory.getChecksum());
		System.out.println("Interpreter number: " + memory.getInterpreterNumber());
		System.out.println("Interpreter version: " + memory.getInterpreterVersion());
		System.out.println("Screen height (lines): " + memory.getScreenHeight());
		System.out.println("Screen width (chars): " + memory.getScreenWidth());
		System.out.println("Screen width (units): " + memory.getScreenWidthInUnits());
		System.out.println("Screen height (units): " + memory.getScreenHeightInUnits());
		System.out.println("Font width (units): " + memory.getFontWidth());
		System.out.println("Font height (units): " + memory.getFontHeight());
		System.out.println("Routines offset: " + memory.getRoutinesOffset());
		System.out.println("Static strings offset: " + memory.getStaticStringsOffset());
		
		//int[] data = {5002, 25376, 1348, 13978, 57669};	
		//ZSCII frotz = new V3ZSCII(memory);
		//String string = frotz.decode(data);
		//System.out.println("~" + string + "~");
		
		ZSCII zscii = new V3ZSCII(memory);
		byte[] boo = zscii.encode("i");
		int[] data = {14501, 38053};
		String zstr = zscii.decode(data);
		System.out.println("~" + zstr + "~");
		
		ObjectTable ot = new ObjectTable(memory);
		ObjectTableObject oto = ot.getObject(180);
		System.out.println(oto);
		
		//OTO toto = ot.getOTO(156);
		//System.out.println(toto);
	}

	public static void main(String[] args) {
		// GUI interface.
		//new LeafpileGUI();
		
		// cli
		Leafpile lp = new Leafpile();
		lp.cli();
	}

}
