package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Clear_attr (clear attribute) instructions. See p. 82.
 * 
 * clear_attr object attribute
 * 
 * Make <b>object</b> not have the attribute numbered <b>attribute</b>.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 August 2016) 
 */
public class Clear_attr extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Clear_attr(Instruction instruction) {
		super(instruction);
		name = "clear_attr";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int obj = operands.get(0);
		int attribute = operands.get(1);
				
		ObjectTableObject oto = zmachine.getFactory().retrieveObject(obj);
		
		oto.clearAttribute(attribute);
		
		{
			System.out.println("CLEAR_ATTR object:" + obj + " attribute:" + attribute);
			System.out.println();
		}
		
		// TODO: for testing only, too compare ObjectTableObjects
		// before and after setting attribute.
		//ObjectTableObject foo = factory.createObject(obj);
		//System.out.println();
		
	}
	
}
