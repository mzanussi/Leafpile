package com.michaelzanussi.leafpile.opcodes;

import java.util.List;

import com.michaelzanussi.leafpile.instructions.Instruction;
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
		current.setVariableValue(current.getStoreVariable(), memory.unsigned(result));
		
		{
			//System.out.println(current.getPC() + " ADD op1:" + op1 + " op2:" + op2 + " result:" + result + " store:" + current.getStore() + " (" + current.getVariableValue(current.getStore()) + ")");
			System.out.println("ADD op1:" + op1 + " op2:" + op2 + " result:" + result + " store:" + current.getStoreVariable() + " (" + current.getVariableValue(current.getStoreVariable()) + ")");
			System.out.print("local vars now = ");
			int[] foo = current.getLocals();
			for (int i = 0; i < foo.length; i++) {
				System.out.print(foo[i] + " ");
			}
			System.out.println("\n");
		}
		
	}
		
}
