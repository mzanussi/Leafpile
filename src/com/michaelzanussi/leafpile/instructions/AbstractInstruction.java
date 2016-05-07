package com.michaelzanussi.leafpile.instructions;

import java.util.ArrayList;
import java.util.List;

import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zmachine.Rous;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This class provides a skeletal implementation of the <code>Instruction</code> 
 * interface, to minimize the effort required to implement this interface.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (5 May 2016) 
 */
public abstract class AbstractInstruction implements Instruction {
	
	protected Zmachine zmachine;
	protected Memory memory;
	protected Rous rous;
	
	protected Form form;				// instruction form
	protected Opcount opcount;			// operand count
	//protected int start_pc;
	protected int opcode_byte;			// opcode byte
	protected int opcode_no;			// opcode number
	protected Opcode opcode;			// the opcode
	protected List<Integer> operands;	// operands

	// Operand type
	protected static final int OTYPE_LC = 0;
	protected static final int OTYPE_SC = 1;
	protected static final int OTYPE_VAR = 2;
	protected static final int OTYPE_OMITTED = 3;

	AbstractInstruction(Zmachine zmachine) {
		
		this.zmachine = zmachine;
		
		memory = zmachine.memory();
		rous = zmachine.rous();
		
		operands = new ArrayList<Integer>();
		
		// Fetch the opcode byte.
		opcode_byte = zmachine.memory().getByte(rous.getPC());
		rous.setPC(rous.getPC() + 1);
		
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Instruction#getOpcount()
	 */
	public Opcount getOpcount() {
		return opcount;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Instruction#getOpcodeNumber()
	 */
	public int getOpcodeNumber() {
		return opcode_no;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Instruction#getOpcodeByte()
	 */
	public int getOpcodeByte() {
		return opcode_byte;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Instruction#getZmachine()
	 */
	public Zmachine getZmachine() {
		return zmachine;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Instruction#exec()
	 */
	public abstract void exec();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Instruction form: " + form + "\n");
		sb.append("Operand count: " + opcount + "\n");
		sb.append("Opcode byte: " + opcode_byte + "\n");
		sb.append("Opcode: " + opcode_no + "\n");
		sb.append("Current PC: " + rous.getPC() + "\n");
		sb.append(opcode + "\n");
		return sb.toString();
	}

}
