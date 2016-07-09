package com.michaelzanussi.leafpile.opcodes;

import java.util.List;

import com.michaelzanussi.leafpile.factory.Factory;
import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zmachine.Rous;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This class provides a skeletal implementation of the <code>Opcode</code> 
 * interface, to minimize the effort required to implement this interface.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public abstract class AbstractOpcode implements Opcode {

	protected Zmachine zmachine;
	protected Instruction instruction;
	protected Memory memory;
	protected Rous current;				// current routine state
	protected List<Integer> operands;	// operands
	protected Factory factory;			// object factory
	
	protected int version;
	
	protected String name;
	
	protected boolean isStore;
	protected boolean isBranch;
	
	protected static final int FALSE = 0;
	protected static final int TRUE = 1;
	
	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the Instruction object.
	 */
	public AbstractOpcode(Instruction instruction) {
		this.instruction = instruction;
		zmachine = instruction.getZmachine();
		memory = zmachine.memory();
		current = zmachine.getCurrentRous();
		operands = instruction.getOperands();
		version = memory.getVersion();
		factory = new Factory(zmachine);
		isStore = false;
		isBranch = false;
	}
	
	/**
	 * Executes a branch based on the condition argument.
	 * 
	 * @param condition the condition
	 */
	protected void executeBranch(boolean condition) {
		
		boolean branch_when = instruction.branchWhen();
		
		// Get the branch offset.
		int offset = instruction.getBranchOffset();
		
		{
			System.out.print("offset:" + offset + " when:" + branch_when + " ");
		}
		
		if ((branch_when == true && condition == true) || (branch_when == false && condition == false)) {
			
			// An offset of 0 means return False from the current routine
			// and 1 means return True from the current routine. (4.7.1)
			if (offset == 0) {
				retuurn(FALSE);
			}
			else if (offset == 1) {
				retuurn(TRUE);
			}
			
			// Calculate new program counter based on the offset. (4.7.2)
			int newPC = current.getPC() + offset - 2;
			// Set new PC.
			current.setPC(newPC);

			{
				System.out.print("pc:" + newPC);
			}

		}

		{
			System.out.println();
		}
		
	}
	
	/**
	 * Return from current routine.
	 * 
	 * @param retval
	 */
	protected void retuurn(int retval) {
		// Try to pop the call stack.
		Rous previous = zmachine.previousRoutine();
		// Set the result value.
		previous.setVariableValue(previous.getStoreVariable(), retval);
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Opcode#isStore()
	 */
	@Override
	public boolean isStore() {
		return isStore;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.Opcode#isBranch()
	 */
	@Override
	public boolean isBranch() {
		return isBranch;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Opcode#exec()
	 */
	@Override
	public abstract void exec();
	
}
