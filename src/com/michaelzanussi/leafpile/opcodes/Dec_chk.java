package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Dec_chk (decrement then branch) instructions. See p. 83.
 * 
 * dec_chk (variable) value ?(label)
 * 
 * Decrement variable, and branch if it is now less than the given value.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (7 June 2016) 
 */
public class Dec_chk extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Dec_chk(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "dec_chk";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int variable = operands.get(0);
		int value = memory.signed(operands.get(1));
		
		// Retrieve the value at variable, increment it, and put back.
		int nv = memory.signed(current.getVariableValue(variable));
		nv--;
		current.setVariableValue(variable, memory.unsigned(nv));
		
		boolean result = nv < value;

		{
			System.out.print("DEC_CHK var:" + variable + " (dec:" + nv + ") value:" + value + " ");
			System.out.print("result:" + result + " ");
		}
		
		// Branch on condition.
		executeBranch(result);
		
		{
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
