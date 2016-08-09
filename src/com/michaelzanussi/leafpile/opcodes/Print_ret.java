package com.michaelzanussi.leafpile.opcodes;

import java.util.ArrayList;
import java.util.List;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.zscii.ZSCII;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Print_ret (print string then return) instructions. See p. 91.
 * 
 * print_ret
 * 
 * Print the quoted (literal) Z-encoded string, then print a new-line, and
 * then return true (i.e., 1).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 August 2016) 
 */
public class Print_ret extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Print_ret(Instruction instruction) {
		super(instruction);
		name = "print_ret";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		ZSCII zscii = zmachine.getFactory().createZSCII();
		List<Integer> array = new ArrayList<Integer>();
		
		// Text opcodes. Read in the text string starting at PC. The text 
		// string is stored according to the usual rules: in particular 
		// execution continues after the last 2-byte word of text (the one 
		// with top bit set). (4.8)
				
		byte stopbit = 0;	// bit 15
		
		// get words (ints) until stopbit is reached,
		// putting those words into an int array
		do {
			int data = memory.getWord(current.getPC());
			current.setPC(current.getPC() + 2);
			array.add(data);
			stopbit = (byte)(data >> 15);
		} while (stopbit == 0);
		
		int[] words = new int[array.size()];
		for (int i = 0; i < array.size(); i++) {
			words[i] = array.get(i);
		}
		
		// decode it
		String string = zscii.decode(words);
		zmachine.ui().write(string);
		
		{
			System.out.println("PRINT ~" + string + "~");
			System.out.println();
		}
		
		// TODO: everything above comes directly from Print opcode,
		// so it all should be moved to the superclass I think.
		// What follows is unique to Print_ret.
		zmachine.ui().write("\n");
		ret(1);
		
	}
		
}
