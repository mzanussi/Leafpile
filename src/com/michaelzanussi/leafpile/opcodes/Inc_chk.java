package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Inc_chk (increment then branch) instructions. See p. 86.
 * 
 * inc_chk (variable) value ?(label)
 * 
 * Increment variable, and branch if now greater than value.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class Inc_chk extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Inc_chk(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "inc_chk";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int variable = operands.get(0);
		int value = operands.get(1);
		
		// Retrieve the value at variable, increment it, and put back.
		int nv = memory.signed(current.getVariableValue(variable));
		nv++;
		current.setVariableValue(variable, memory.unsigned(nv));
		
		boolean result = nv > value;

		{
			System.out.print("INC_CHK var:" + variable + " (inc:" + nv + ") value:" + value + " ");
			System.out.print("result:" + result + " ");
		}
		
		// Branch on condition.
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}
	
}
