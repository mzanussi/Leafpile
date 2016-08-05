package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Set_text_style (set text style) instructions. See p. 100.
 * 
 * set_text_style style
 * 
 * Sets the text style to: Roman (if 0), Reverse Video (if 1), Bold (if 2),
 * Italic (4), Fixed Pitch (8). In some interpreters (though this is not
 * required) a combination of styles is possible (such as reverse video and
 * bold). In these, changing to Roman should turn off all the other styles
 * currently set.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (1 June 2016) 
 */
public class Set_text_style extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Set_text_style(Instruction instruction) {
		super(instruction);
		name = "set_text_style";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int style = operands.get(0);
		
		{
			System.out.println("SET_TEXT_STYLE style:" + style);
			System.out.println();
		}
		
		zmachine.ui().flush_buf();
		zmachine.ui().set_text_style(style);
		// TODO: set memory 0x26, 0x27
		
	}
		
}
