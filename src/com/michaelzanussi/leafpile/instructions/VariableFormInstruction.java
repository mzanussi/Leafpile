package com.michaelzanussi.leafpile.instructions;

import com.michaelzanussi.leafpile.factory.Factory;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * A concrete implementation of a variable form instruction.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (5 May 2016) 
 */
public class VariableFormInstruction extends AbstractInstruction {
	
	/**
	 * Single-arg constructor takes Z-machine object as only arg.
	 * 
	 * @param zmachine the Z-machine object
	 */
	public VariableFormInstruction(Zmachine zmachine) {
		
		super(zmachine);

		// set the instruction form (4.3)
		form = Form.VAR;
		
		// Bit 5 is used to determine operand count. (4.3.3)
		int bit5 = (opcode_byte & 0x20) >> 5;
		if (bit5 == 0) {
			opcount = Opcount.O_2OP;
		} else {
			opcount = Opcount.O_VAR;
		}
		
		// Get the opcode. (4.3.3)
		opcode_no = (opcode_byte & 0x1f);
		
		// If the opcode is 0x0c (call_vs2) or 0x1a (call_vn2),
		// then there can be up to 8 possible operands whose types
		// are specified in 2 bytes; otherwise, there can be up
		// to 4 possible operands, and their types are specified in
		// 1 byte. (4.4.3, 4.4.3.1)
		int otypebyte = 0;
		int poss_ops = 0;
		if (opcode_no == 0x0c || opcode_no == 0x1a) {
			poss_ops = 8;
			otypebyte = memory.getWord(current.getPC());
			current.setPC(current.getPC() + 2);
		} else {
			poss_ops = 4;
			otypebyte = memory.getByte(current.getPC());
			current.setPC(current.getPC() + 1);
		}
		
		// Now retrieve the operands and store them in an array.
		// Once "omitted" type is found, then we are done. (4.5)
		for (int i = 0; i < poss_ops; i++) {
			int otype = (otypebyte >> ((poss_ops * 2 - 2) - (i * 2))) & 0x03;
			if (otype == OTYPE_OMITTED) {
				break;
			}
			switch (otype) {
			case OTYPE_LC:	// large constant (0-65535)
				operands.add(memory.getWord(current.getPC()));
				current.setPC(current.getPC() + 2);
				break;
			case OTYPE_SC:	// small constant (0-255)
				operands.add(memory.getByte(current.getPC()));
				current.setPC(current.getPC() + 1);
				break;
			case OTYPE_VAR:	// variable value
				int variable = memory.getByte(current.getPC());
				current.setPC(current.getPC() + 1);
				int value = current.getVariableValue(variable);
				operands.add(value);
				break;
			}
		}
		
		// Create the opcode object.
		Factory factory = new Factory(zmachine);
		opcode = factory.createOpcode(this);
		
		// The variable number of where to put result. (4.6)
		if (opcode.isStore()) {
			int store = memory.getByte(current.getPC());
			current.setPC(current.getPC() + 1);
			current.setStoreVariable(store);
		}

		// Branches. (4.7)
		if (opcode.isBranch()) {
			setBranch();
		}

	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.AbstractInstruction#exec()
	 */
	public void exec() {
		opcode.exec();
	}

}
