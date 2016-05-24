package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Print_char (print ZSCII char) instructions. See p. 92.
 * 
 * print_char output-character-code
 * 
 * Print a ZSCII character. The operand must be a character code defined
 * in ZSCII for output. In particular, it must certainly not be negative
 * or larger than 1023.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class Print_char extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Print_char(Instruction instruction) {
		super(instruction);
		name = "print_char";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {

		// Retrieve the operand.
		int output_character_code = memory.signed(operands.get(0));
		
		// Output the character.
		Character ch = new Character((char)output_character_code);
		String string = ch.toString();
		zmachine.ui().write(string);

		{
			System.out.println("PRINT_CHAR ~" + string + "~");
			System.out.println();
		}
		
	}
	
}
