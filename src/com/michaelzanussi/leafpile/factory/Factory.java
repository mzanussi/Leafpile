package com.michaelzanussi.leafpile.factory;

import com.michaelzanussi.leafpile.instructions.Call;
import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.instructions.Instruction.Opcount;
import com.michaelzanussi.leafpile.instructions.Opcode;
import com.michaelzanussi.leafpile.instructions.VariableFormInstruction;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public class Factory {
	
	private Zmachine zmachine;
	
	public Factory(Zmachine zmachine) {
		this.zmachine = zmachine;
	}
	
	/**
	 * Creates an instruction (long, short, extended, or variable).
	 * 
	 * @return an instruction.
	 */
	public Instruction createInstruction() {
		// get the current routine's pc
		int pc = zmachine.rous().getPC();
		// get the opcode byte
		int obyte = zmachine.memory().getByte(pc);
		// get bits 6 & 7 to determine form (4.3)
		int bits67 = obyte >> 6;
		
		if (bits67 == 3) {				// variable form
			return new VariableFormInstruction(zmachine);
		} else if (obyte == 0xbe) {		// extended form
			assert (false) : "extended form";
		} else if (bits67 == 2) {		// short form
			assert (false) : "short form";
		} else {						// long form
			assert (false) : "long form";
		}
		return null;
	}
	
	/**
	 * Creates an opcode object based on the operand count
	 * (0OP, 1OP, 2OP, or VAR) and the opcode number.
	 * 
	 * @param opcount the opcode count
	 * @param opcode the opcode number
	 * @return an opcode object
	 */
	public Opcode createOpcode(Instruction instruction) {
		
		Opcount opcount = instruction.getOpcount();
		int opcode_no = instruction.getOpcodeNumber();
		
		switch (opcount) {
		case O_0OP:
			switch (opcode_no) {
			default:
				assert (false) : "unimplemented 0OP opcode: " + opcode_no;
			}
			break;
		case O_1OP:
			switch (opcode_no) {
			default:
				assert (false) : "unimplemented 1OP opcode: " + opcode_no;
			}
			break;
		case O_2OP:
			switch (opcode_no) {
			default:
				assert (false) : "unimplemented 2OP opcode: " + opcode_no;
			}
			break;
		case O_VAR:
			switch (opcode_no) {
			case 0x00:
				if (zmachine.memory().getVersion() <= 3) {
					return new Call(instruction);
				} else {
					assert (false) : "call_vs not implemented";
				}
			default:
				assert (false) : "unimplemented VAR opcode: " + opcode_no;
			}
			break;
		}
		
		return null;
	}

}
