package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Put_prop (put property) instructions. See p. 94.
 * 
 * put_prop object property value
 * 
 * Writes the given value to the given property of the given object. If the
 * property does not exist for that object, the interpreter should halt
 * with a suitable error message. If the property length is 1, then the
 * interpreter should store only least significant byte of the value. (For
 * instance, storing -1 into a 1-byte property results in the property value
 * 255.) As with get_prop the property length must not be more than 2: if it
 * is, the behavior of the opcode is undefined.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (13 May 2016) 
 */
public class Put_prop extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Put_prop(Instruction instruction) {
		super(instruction);
		name = "put_prop";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
	
		// Retrieve the operands.
		int obj_num = operands.get(0);
		int property = operands.get(1);
		int value = operands.get(2);
		
		ObjectTableObject oto = zmachine.getFactory().retrieveObject(obj_num);
		oto.setProperty(property, value);
		
		{
			System.out.println("PUT_PROP object:" + obj_num + " property:" + property + " value:" + value);
			System.out.println();
		}

	}
		
}
