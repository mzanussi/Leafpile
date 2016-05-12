package com.michaelzanussi.leafpile.opcodes;

import java.util.List;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (11 May 2016) 
 */
public class Je extends AbstractOpcode {

	public Je(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "je";
	}
	
	public void exec() {
		
		// Retrieve the operands.
		List<Integer> operands = instruction.getOperands();
		
		if (operands.size() < 2) {
			assert(false);
		}
		
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
