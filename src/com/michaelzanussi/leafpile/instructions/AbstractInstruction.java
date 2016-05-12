package com.michaelzanussi.leafpile.instructions;

import java.util.ArrayList;
import java.util.List;

import com.michaelzanussi.leafpile.opcodes.Opcode;
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
	protected Rous current;
	
	protected Form form;				// instruction form
	protected Opcount opcount;			// operand count
	protected int start_pc;				// start PC
	protected int opcode_byte;			// opcode byte
	protected int opcode_no;			// opcode number
	protected Opcode opcode;			// the opcode
	protected List<Integer> operands;	// operands

	protected boolean branchWhenTrue;	// branch when true?
	protected boolean branchWhenFalse;	// branch when false?
	protected int branchOffset;			// branch offset
	
	// Operand type
	protected static final int OTYPE_LC = 0;
	protected static final int OTYPE_SC = 1;
	protected static final int OTYPE_VAR = 2;
	protected static final int OTYPE_OMITTED = 3;

	AbstractInstruction(Zmachine zmachine) {
		
		this.zmachine = zmachine;
		
		memory = zmachine.memory();
		current = zmachine.getCurrentRous();
		
		operands = new ArrayList<Integer>();
		branchWhenTrue = false;
		branchWhenFalse = false;
		branchOffset = 0;
		
		// save the start pc
		start_pc = current.getPC();
		
		// Fetch the opcode byte.
		opcode_byte = zmachine.memory().getByte(current.getPC());
		current.setPC(current.getPC() + 1);
		
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.Instruction#branchWhen()
	 */
	public boolean branchWhen() {
		if (branchWhenFalse) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isBranch() {
		return (branchWhenFalse || branchWhenTrue);
	}
	
	public int getBranchOffset() {
		return branchOffset;
	}
	
	/**
	 * Set branch conditions and offset. (4.7)
	 */
	protected void setBranch() {
		// get first byte of branch information
		int br = memory.getByte(current.getPC());
		current.setPC(current.getPC() + 1);
		// Test bit 7, to see if branch occurs when the condition is false,
		// or the branch is on true.
		if ((br & 0x80) == 0x80) {
			// Branch occurs when true
			/*current.*/setBranchWhenTrue(true);
		}
		else {
			// Branch occurs when false
			/*current.*/setBranchWhenFalse(true);
		}
		// Test bit 6. If set the branch occupies 1 byte and offset is in
		// range 0-63, given by bottom 6 bits (0-5); otherwise, the offset 
		// is a signed 14-bit number given by the bottom 6 bits (0-5)
		// and all 8 bits of branch byte #2.
		if ((br & 0x40) == 0x40) {
			// Branch occupies 1 byte, offset in range of 0-63,
			// given in bottom 6 bits.
			/*current.*/setBranchOffset(br & 0x3f);
		}
		else {
			// Offset is a 14-bit signed integer, given by first
			// 6 bits of 1st byte and all 8 bits of 2nd byte.
			int br2 = memory.getByte(current.getPC());
			current.setPC(current.getPC() + 1);
			short offset = (short)(((br & 0x3f) << 8) | br2);
			// Since value is signed, check if bit 13 is set.
			// If set, convert to 16-bit signed.
			if ((offset & 0x2000) == 0x2000) {
				offset |= 0xC000;
			}
			/*current.*/setBranchOffset(offset);
		}
	}
	
	public void setBranchOffset(int branchOffset) {
		this.branchOffset = branchOffset;
	}

	public void setBranchWhenFalse(boolean branchWhenFalse) {
		this.branchWhenFalse = branchWhenFalse;
	}

	public void setBranchWhenTrue(boolean branchWhenTrue) {
		this.branchWhenTrue = branchWhenTrue;
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
	 * @see com.michaelzanussi.leafpile.instructions.Instruction#getOperands()
	 */
	public List<Integer> getOperands() {
		return operands;
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
		sb.append("@" + start_pc + " " + form + " " + opcount + " obyte=" + opcode_byte + " opcode=" + opcode_no + " operands={");
		StringBuilder temp = new StringBuilder();
		for (Integer operand : operands) {
			temp.append(operand + ",");
		}
		sb.append(temp.substring(0, temp.length() - 1) + "}\n");
		sb.append("cur rous PC: " + current.getPC() + "\n");
		sb.append("cur rous Local vars: ");
		int[] foo = current.getLocals();
		for (int i = 0; i < foo.length; i++) {
			sb.append(foo[i] + " ");
		}
		sb.append("\n");
		sb.append("cur rous Arguments: " + current.getNumberOfArgs() + "\n");
		sb.append("cur rous Store: " + current.getStoreVariable() + "\n");
		sb.append("cur rous Branch: " + /*current.*/isBranch() + " ");
		if (/*current.*/isBranch()) {
			sb.append("(branch when " + /*current.*/branchWhen() + ")");
		}
		sb.append("\n");
		//sb.append(opcode + "\n");
		return sb.toString();
	}

}
