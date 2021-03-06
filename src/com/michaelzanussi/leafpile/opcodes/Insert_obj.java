package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Insert_obj (insert object into tree) instructions. See p. 86.
 * 
 * insert_obj object destination
 * 
 * Moves object O to become the first child of the destination object D.
 * (Thus, after the operation the child of D is O, and the sibling of O
 * is whatever was previously the child of D.) All children of O move
 * with it. (Initially O can be at any point in the object tree; it may
 * legally have parent zero.)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (23 May 2016) 
 */
public class Insert_obj extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Insert_obj(Instruction instruction) {
		super(instruction);
		name = "insert_obj";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int obj = operands.get(0);
		int dest = operands.get(1);
		
		// Move object 'obj' to become first child of 'dest'.
		
		// Get 'obj' and find its parent. If a parent exists,
		// (parent not 0) first remove 'obj' from the tree, 
		// and then move it to its new position.
		
		ObjectTableObject obj_oto = zmachine.getFactory().retrieveObject(obj);
		int obj_parent = obj_oto.getParent();
		
		if (obj_parent != 0) {
			obj_oto.remove();
		}
		
		// Now, find out child of 'dest' and save it, because
		// first child of 'dest' is going to be 'obj'. The
		// current child of 'dest' will be new sibling of 'obj'.
		
		ObjectTableObject dest_oto = zmachine.getFactory().retrieveObject(dest);
		int dest_obj_child = dest_oto.getChild();
		obj_oto.setSibling(dest_obj_child);
		
		// Finally, set parent of 'obj' to 'dest' and child of
		// 'dest' to 'obj'.
		
		obj_oto.setParent(dest);
		dest_oto.setChild(obj);
		
		{
			System.out.print("INSERT_OBJ object:" + obj + " ");
			System.out.println("destination:" + dest);
			System.out.println();
		}
		
	}
	
}
