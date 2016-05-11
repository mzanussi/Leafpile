package com.michaelzanussi.leafpile.instructions;

import java.util.List;

import com.michaelzanussi.leafpile.zmachine.Rous;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (10 May 2016) 
 */
public class Add extends AbstractOpcode {

	public Add(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "add";
	}
	
	public void exec() {
		
		Rous current = zmachine.getCurrentRous();
		
		// Retrieve the operands.
		List<Integer> operands = instruction.getOperands();
		int op1 = memory.signed(operands.get(0));
		int op2 = memory.signed(operands.get(1));
		
		// Perform the signed addition and store unsigned.
		int result = op1 + op2;
		current.setVariable(current.getStore(), memory.unsigned(result));
		
	}
		
}
