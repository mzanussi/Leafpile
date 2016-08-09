package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.zmachine.Rous;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Call_1s (call routine, store retval) instructions. See p. 80.
 * 
 * call_1s routine -> (result)
 * 
 * Stores routine().
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (25 May 2016) 
 */
public class Call_1s extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Call_1s(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "call_1s";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// First operand is packed address to routine start address. (5.1)
		int routine_addr = memory.unpackAddress(operands.get(0));
		
		// A routine call to packed address 0 is legal: 
		// it does nothing and returns false (0). (6.4.3)
		if (routine_addr == 0) {
			current.setVariableValue(current.getStoreVariable(), 0);
			return;
		}

		// Header. Number of local variables. (5.2)
		int num_of_locals = memory.getByte(routine_addr++);
		
		// Create an array of local variables. (5.2)
		int[] locals = new int[15];

		// Initialize the local variables. Only necessary for
		// version <= 4 since locals array already initialized
		// to zeroes for version >= 5. (5.2.1)
		if (memory.getVersion() <= 4) {
			for (int i = 0; i < num_of_locals; i++) {
				locals[i] = memory.getWord(routine_addr);
				routine_addr += 2;
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
			System.out.print("CALL_1S addr:");
			System.out.print(routine_addr + " args:");
			for (int x = 0; x < 7; x++) {
				System.out.print(locals[x] + " ");
			}
			System.out.println("(args=" + num_of_args + ",lcls=" + num_of_locals + ") ");
			System.out.println();
		}
		
		// Create new routine state for the new routine. Execution
		// of instructions begins from the byte after the header
		// information, currently pointed to by addr. This is the new PC.
		Rous rous = zmachine.createRoutine(routine_addr);
		rous.setLocals(locals);
		rous.setNumberOfArgs(num_of_args > num_of_locals ? num_of_locals : num_of_args);
		
	}

}
