package com.michaelzanussi.leafpile.opcodes;

import java.util.List;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.zmachine.Rous;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public class Call extends AbstractOpcode {
	
	public Call(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "call";
	}
	
	public void exec() {
		
		List<Integer> operands = instruction.getOperands();
		
		// First operand is packed address to routine start address. (5.1)
		int addr = operands.get(0) * 2;
		
		// TODO: A routine call to packed address 0 is legal: 
		// it does nothing returns false (0). (6.4.3)
		if (addr == 0) {
			assert (false) : "finish Call exec for packed address 0";
		}

		// Header. Number of local variables. (5.2)
		int num_of_locals = memory.getByte(addr++);
		
		// Create an array of local variables. (5.2)
		int[] locals = new int[15];

		// Initialize the local variables. Only necessary for
		// version <= 4 since locals array already initialized
		// to zeroes for version >= 5. (5.2.1)
		if (memory.getVersion() <= 4) {
			for (int i = 0; i < num_of_locals; i++) {
				locals[i] = memory.getWord(addr);
				addr += 2;
			}
		} 
				
		// Write the routine arguments into the local variables 
		// table. (6.4.4) It is legal for there to be more arguments
		// than local variables (any spare arguments are thrown
		// away) or for there to be fewer. (6.4.4.1)
		int num_of_args = operands.size() - 1;
		for (int i = 0; i < num_of_args; i++) {
			if (i < num_of_locals) {
				locals[i] = operands.get(i + 1);
			}
		}
		
		{
			System.out.print("CALL addr:");
			System.out.print((operands.get(0) * 2) + " args:");
			for (int x = 0; x < 7; x++) {
				System.out.print(locals[x] + " ");
			}
			System.out.print("(args=" + num_of_args + ",lcls=" + num_of_locals + ") ");
			System.out.println("\n");
		}
		
		// Create new routine state for the new routine. Execution
		// of instructions begins from the byte after the header
		// information, currently pointed to by addr. This is the new PC.
		Rous rous = zmachine.createRoutine(addr);
		rous.setLocals(locals);
		rous.setNumberOfArgs(num_of_args > num_of_locals ? num_of_locals : num_of_args);
		
	}

}
