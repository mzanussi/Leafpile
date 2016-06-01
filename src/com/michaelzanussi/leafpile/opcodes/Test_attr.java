package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Test_attr (jump if object has attribute) instructions. See p. 103.
 * 
 * test_attr object attribute ?(label)
 * 
 * Jump if object has attribute.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (16 May 2016) 
 */
public class Test_attr extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Test_attr(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "store";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int obj = operands.get(0);
		int attribute = operands.get(1);
				
		ObjectTableObject oto = factory.createObject(obj);
		
		boolean result = oto.isAttributeSet(attribute);
		
		{
			System.out.print("TEST_ATTR object:" + obj + " attribute:" + attribute + " isSet:" + result + " ");
		}
		
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}
	
}
