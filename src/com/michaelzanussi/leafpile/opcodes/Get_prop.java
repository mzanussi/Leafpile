package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Get_prop (get property) instructions. See p. 85.
 * 
 * get_prop object property -> (result)
 * 
 * Read property from object (resulting in the default value if it had no
 * such declared property). If the property has length 1, the value is only
 * that byte. If it has length 2, the first two bytes of the property are
 * taken as a word value. It is illegal for the opcode to be used if the
 * property has length greater than 2, and the result is unspecified.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 July 2016) 
 */
public class Get_prop extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Get_prop(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "get_prop";
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
		ObjectTableObject oto = factory.createObject(obj);
		int result = oto.getProperty(property);
		current.setVariableValue(current.getStoreVariable(), result);
		
		{
			System.out.println("GET_PROP obj:" + obj + " property:" + property + " result:" + result + " store:" + current.getStoreVariable());
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
