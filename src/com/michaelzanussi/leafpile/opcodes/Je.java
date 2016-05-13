package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Je (jump if equal) instructions. See p. 86.
 * 
 * je a b ?(label)
 * 
 * Jump if op1 is equal to any of the subsequent operands. (Thus JE op1
 * never jumps and JE op1 op2 jumps if op1 = op2.)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (11 May 2016) 
 */
public class Je extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Je(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "je";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	public void exec() {
		
		// Retrieve the operand.
		int op1 = operands.get(0);

		boolean result = false;
		
		for (int i = 1; i < operands.size(); i++) {
			// Branch on True occurs if first operand 
			// equals any of the subsequent operands.
			int op2 = operands.get(i);
			if (op1 == op2) {
				result = true;
				break;
			}
		}
		
		{
			System.out.print("JE op1:" + op1 + " ");
			for (int j = 1; j < operands.size(); j++) {
				System.out.print("op" + j + ":" + operands.get(j) + " ");
			}
			System.out.print("result:" + result + " ");
		}
		
		executeBranch(result);
		
	}
	
}
