package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Print_obj (print object) instructions. See p. 92.
 * 
 * print_obj object
 * 
 * Print short name of object (the Z-encoded string in the object header,
 * not a property). If the object number is invalid, the intepreter should
 * halt with a suitable error message.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 July 2016) 
 */
public class Print_obj extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Print_obj(Instruction instruction) {
		super(instruction);
		name = "print_obj";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int object = operands.get(0);
		
		ObjectTableObject oto = zmachine.getFactory().retrieveObject(object);
		String shortName = oto.getShortName();
		zmachine.ui().write(shortName);
		
		{
			System.out.println("PRINT_OBJ object:" + object + " (short name=~" + shortName + "~)");
			System.out.println();
		}
		
	}
		
}
