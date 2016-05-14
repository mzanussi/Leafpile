package com.michaelzanussi.leafpile.factory;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.instructions.Instruction.Opcount;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;
import com.michaelzanussi.leafpile.objecttable.V1Object;
import com.michaelzanussi.leafpile.objecttable.V4Object;
import com.michaelzanussi.leafpile.opcodes.Add;
import com.michaelzanussi.leafpile.opcodes.Call;
import com.michaelzanussi.leafpile.opcodes.Je;
import com.michaelzanussi.leafpile.opcodes.Jump;
import com.michaelzanussi.leafpile.opcodes.Jz;
import com.michaelzanussi.leafpile.opcodes.Loadw;
import com.michaelzanussi.leafpile.opcodes.Opcode;
import com.michaelzanussi.leafpile.opcodes.Put_prop;
import com.michaelzanussi.leafpile.opcodes.Ret;
import com.michaelzanussi.leafpile.opcodes.Storew;
import com.michaelzanussi.leafpile.opcodes.Sub;
import com.michaelzanussi.leafpile.instructions.LongFormInstruction;
import com.michaelzanussi.leafpile.instructions.ShortFormInstruction;
import com.michaelzanussi.leafpile.instructions.VariableFormInstruction;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * A factory class for instantiating various kinds of objects.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public class Factory {
	
	private Zmachine zmachine;
	
	/**
	 * Single-arg instruction takes a Z-machine as its only arg.
	 * 
	 * @param zmachine the Z-machine
	 */
	public Factory(Zmachine zmachine) {
		this.zmachine = zmachine;
	}
	
	/**
	 * Creates an object table object for the given object number.
	 * 
	 * @param obj_num the object number to create
	 * @return the object table object
	 */
	public ObjectTableObject createObject(int obj_num) {
		if (zmachine.memory().getVersion() < 4) {
			return new V1Object(obj_num, zmachine.memory());
		} else {
			return new V4Object(obj_num, zmachine.memory());
		}
	}
	
	/**
	 * Creates an instruction (long, short, extended, or variable).
	 * 
	 * @return an instruction.
	 */
	public Instruction createInstruction() {
		// get the current routine's pc
		int pc = zmachine.getCurrentRous().getPC();
		// get the opcode byte
		int obyte = zmachine.memory().getByte(pc);
		// get bits 6 & 7 to determine form (4.3)
		int bits67 = obyte >> 6;
		
		if (bits67 == 3) {				// variable form
			return new VariableFormInstruction(zmachine);
		} else if (obyte == 0xbe) {		// extended form
			assert (false) : "extended form";
		} else if (bits67 == 2) {		// short form
			return new ShortFormInstruction(zmachine);
		} else {						// long form
			return new LongFormInstruction(zmachine);
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
				assert (false) : "unimplemented 0OP opcode: 0x" + Integer.toHexString(opcode_no);
			}
			break;
		case O_1OP:
			switch (opcode_no) {
			case 0x00:
				return new Jz(instruction);
			case 0x0b:
				return new Ret(instruction);
			case 0x0c:
				return new Jump(instruction);
			default:
				assert (false) : "unimplemented 1OP opcode: 0x" + Integer.toHexString(opcode_no);
			}
			break;
		case O_2OP:
			switch (opcode_no) {
			case 0x01:
				return new Je(instruction);
			case 0x0f:
				return new Loadw(instruction);
			case 0x14:
				return new Add(instruction);
			case 0x15:
				return new Sub(instruction);
			default:
				assert (false) : "unimplemented 2OP opcode: 0x" + Integer.toHexString(opcode_no);
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
			case 0x01:
				return new Storew(instruction);
			case 0x03:
				return new Put_prop(instruction);
			default:
				assert (false) : "unimplemented VAR opcode: 0x" + Integer.toHexString(opcode_no);
			}
			break;
		}
		
		return null;
	}

}
