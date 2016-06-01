package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Jump (jump unconditionally) instructions. See p. 87.
 * 
 * jump ?(label)
 * 
 * Jump (unconditionally) to the given label. (This is not a branch
 * instruction and the operand is a 2-byte offset to apply to the program
 * counter.) It is legal for this to jump into a different routine
 * (which should not change the routine call state) although it is
 * considered bad practice to do so.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (13 May 2016) 
 */
public class Jump extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Jump(Instruction instruction) {
		super(instruction);
		name = "jump";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int op = operands.get(0);
		
		// return the signed operand
		int signed = memory.signed(op);
		
		// calculate the new pc
		int pc = current.getPC() + signed - 2;
		
		// set the new pc
		current.setPC(pc);
		
		{
			System.out.println("JUMP op:" + op + " (signed=" + signed + ", new pc=" + pc + ")");
			System.out.println();
		}

	}
	
}
