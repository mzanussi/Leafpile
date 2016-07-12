package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Jin (jump if parent) instructions. See p. 87.
 * 
 * jin obj1 obj2 ?(label)
 * 
 * Jump if object a is a direct child of b, i.e., if parent of a is b.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (11 July 2016) 
 */
public class Jin extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Jin(Instruction instruction) {
		super(instruction);
		isBranch = true;
		name = "jin";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int obj1 = operands.get(0);
		int obj2 = operands.get(1);

		// Get object 1's parent.
		ObjectTableObject oto = factory.createObject(obj1);
		int parent = oto.getParent();
		
		// Perform the comparison.
		boolean result = (parent == obj2);
		
		{
			System.out.print("JIN obj1:" + obj1 + " obj2:" + obj2 + " obj1-parent=" + parent + " result:" + result + " ");
		}
		
		// Execute branch.
		executeBranch(result);
		
		{
			System.out.println();
		}
		
	}
	
}
