package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Get_sibling (get sibling) instructions. See p. 85.
 * 
 * get_sibling object -> (result) ?(label)
 * 
 * Get next object in tree, branching if this exists, i.e. is not 0.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 July 2016) 
 */
public class Get_sibling extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Get_sibling(Instruction instruction) {
		super(instruction);
		isBranch = true;
		isStore = true;
		name = "get_sibling";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int obj = operands.get(0);
		
		// Perform the operation and store.
		ObjectTableObject oto = zmachine.getFactory().retrieveObject(obj);
		int sibling = oto.getSibling();
		current.setVariableValue(current.getStoreVariable(), sibling);
		
		// Perform the comparison.
		boolean result = (sibling != 0);
		
		{
			System.out.print("GET_SIBLING obj:" + obj + " sibling:" + sibling + " store:" + current.getStoreVariable() + " ");
			System.out.print("result:" + result + " ");
		}
		
		// Execute branch.
		executeBranch(result);
		
		{
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
