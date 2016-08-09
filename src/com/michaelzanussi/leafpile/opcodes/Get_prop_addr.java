package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Get_prop_addr (get property address) instructions. See p. 85.
 * 
 * get_prop_addr object property -> (result)
 * 
 * Get the byte address (in dynamic memory) of the property data for the given
 * object's property. This must return 0 if the object hasn't got the property.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 August 2016)
 */
public class Get_prop_addr extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Get_prop_addr(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "get_prop_addr";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int obj = operands.get(0);
		int property = operands.get(1);
		
		// Perform the operation and store.
		ObjectTableObject oto = zmachine.getFactory().retrieveObject(obj);
		int result = oto.getPropertyAddress(property);
		current.setVariableValue(current.getStoreVariable(), result);
		
		{
			System.out.println("GET_PROP_ADDR obj:" + obj + " property:" + property + " result:" + result + " store:" + current.getStoreVariable());
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
