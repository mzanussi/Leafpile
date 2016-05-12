package com.michaelzanussi.leafpile.opcodes;

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
	
	protected String name;
	
	protected boolean isStore;
	protected boolean isBranch;
	
	protected static final int FALSE = 0;
	protected static final int TRUE = 1;
	
	public AbstractOpcode(Instruction instruction) {
		this.instruction = instruction;
		zmachine = instruction.getZmachine();
		memory = zmachine.memory();
		isStore = false;
		isBranch = false;
	}
	
	protected void executeBranch(boolean condition) {
		
		Rous current = zmachine.getCurrentRous();
		
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
				System.out.print("newPC:" + newPC);
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
	public boolean isStore() {
		return isStore;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.Opcode#isBranch()
	 */
	public boolean isBranch() {
		return isBranch;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Opcode#exec()
	 */
	public abstract void exec();
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + " " + instruction.getOpcount() + ":" + instruction.getOpcodeByte() + " " + instruction.getOpcodeNumber() + " store:" + isStore + " branch:" + isBranch);
		return sb.toString();
	}
	
}
