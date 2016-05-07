package com.michaelzanussi.leafpile.instructions;

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
	
	protected String name;
	
	protected boolean isStore;
	protected boolean isBranch;
	
	public AbstractOpcode(Instruction instruction) {
		this.instruction = instruction;
		zmachine = instruction.getZmachine();
		isStore = false;
		isBranch = false;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Opcode#exec()
	 */
	public abstract void exec();
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + " " + instruction.getOpcount() + ":" + instruction.getOpcodeByte() + " " + instruction.getOpcodeNumber());
		return sb.toString();
	}
	
}
