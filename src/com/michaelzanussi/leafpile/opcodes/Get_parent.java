package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Get_parent (get parent) instructions. See p. 85.
 * 
 * get_parent object -> (result) ?(label)
 * 
 * Get parent object (note that this has no "branch if exists" clause).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (18 July 2016) 
 */
public class Get_parent extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Get_parent(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "get_parent";
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
		int parent = oto.getParent();
		current.setVariableValue(current.getStoreVariable(), parent);
		
		{
			System.out.print("GET_PARENT obj:" + obj + " parent:" + parent + " store:" + current.getStoreVariable() + " ");
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
