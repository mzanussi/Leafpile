package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Remove_obj (remove object from tree) instructions. See p. 96.
 * 
 * remove_obj object
 * 
 * Detach the object from its parent, so that it no longer has any parent. 
 * (Its children remain in its possession.)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (10 August 2016)
 */
public class Remove_obj extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Remove_obj(Instruction instruction) {
		super(instruction);
		name = "remove_obj";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int obj = operands.get(0);
		
		// Get 'obj' and find its parent. If a parent exists,
		// (parent not 0) remove 'obj' from the tree.
		
		ObjectTableObject obj_oto = zmachine.getFactory().retrieveObject(obj);
		int obj_parent = obj_oto.getParent();
		
		if (obj_parent != 0) {
			obj_oto.remove();
		}
		
		{
			System.out.println("REMOVE_OBJ object:" + obj);
			System.out.println();
		}
		
	}
}
