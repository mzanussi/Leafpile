package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Read_char (read char) instructions. See p. 96.
 * 
 * read_char 1 time routine -> (result)
 * 
 * Reads a single character from input stream 0 (the keyboard). The first
 * operand must be 1 (presumably it was provided to support multiple input
 * devices, but only the keyboard was ever used). time and routine are
 * optional (in Versions 4 and later only) and dealt with as in read above.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (25 May 2016) 
 */
public class Read_char extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Read_char(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "read_char";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// First operand is always 1.
		int one = operands.get(0);
		
		StringBuilder in = new StringBuilder();
		zmachine.ui().read_char(in);
		
		char ch = in.charAt(0);
		current.setVariableValue(current.getStoreVariable(), ch);

		{
			System.out.println("READ_CHAR 1:" + one + " result:~" + in + "~ store:" + current.getStoreVariable());
			System.out.println();
			System.out.print("local vars now = ");
			int[] locals = current.getLocals();
			for (int i = 0; i < locals.length; i++) {
				System.out.print(locals[i] + " ");
			}
			System.out.print("\n");
			System.out.println("stack now = " + current.getStack());
			System.out.println();
		}
		
	}
		
}
