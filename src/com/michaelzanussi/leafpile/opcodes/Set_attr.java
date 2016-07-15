package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Set_attr (set attribute) instructions. See p. 99.
 * 
 * set_attr object attribute
 * 
 * Make <b>object</b> have the attribute numbered <b>attribute</b>.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (11 July 2016) 
 */
public class Set_attr extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Set_attr(Instruction instruction) {
		super(instruction);
		name = "set_attr";
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
		
		oto.setAttribute(attribute);
		
		{
			System.out.println("SET_ATTR object:" + obj + " attribute:" + attribute);
			System.out.println();
		}
		
		// TODO: for testing only, too compare ObjectTableObjects
		// before and after setting attribute.
		//ObjectTableObject foo = factory.createObject(obj);
		//System.out.println();
		
	}
	
}
