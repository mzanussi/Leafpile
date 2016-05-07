package com.michaelzanussi.leafpile.instructions;

import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This interface defines an instruction.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (5 May 2016) 
 */
public interface Instruction {

	// Defines the operand count.
	public enum Opcount {O_0OP, O_1OP, O_2OP, O_VAR};
	
	// Defines the instruction form.
	public enum Form {LONG, SHORT, EXT, VAR};
	
	/**
	 * Execute the instruction.
	 */
	public void exec();
	
	/**
	 * Returns the operand count (0OP, 1OP, 2OP, or VAR).
	 * 
	 * @return the operand count
	 */
	public Opcount getOpcount();
	
	/**
	 * Returns the opcode number for this instruction.
	 * 
	 * @return the opcode number
	 */
	public int getOpcodeNumber();
	
	/**
	 * Returns the opcode byte for this instruction.
	 * Mainly for debugging purposes.
	 * 
	 * @return the opcode byte
	 */
	public int getOpcodeByte();
	
	/**
	 * Returns the Z-machine.
	 * 
	 * @return the Z-machine.
	 */
	public Zmachine getZmachine();

}
