package com.michaelzanussi.leafpile.instructions;

import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * A concrete implementation of a short form instruction.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 May 2016) 
 */
public class ShortFormInstruction extends AbstractInstruction {

	/**
	 * Single-arg constructor takes Z-machine object as only arg.
	 * 
	 * @param zmachine the Z-machine object
	 */
	public ShortFormInstruction(Zmachine zmachine) {
		
		super(zmachine);
		
		// set the instruction form (4.3)
		form = Form.SHORT;
		
		// operand count is 2OP. (4.3.1)
		opcount = ((opcode_byte & 0x30) == 0x30) ? Opcount.O_0OP : Opcount.O_1OP;

		// Get the opcode. (4.3.1)
		opcode_no = (opcode_byte & 0x0f);
		
		// Bits 4 and 5 (0x30) of the opcode give the operand type. (4.4.1, 4.5)
		switch(opcode_byte & 0x30) {
		case 0x00:	// large
			operands.add(memory.getWord(current.getPC()));
			current.setPC(current.getPC() + 2);
			break;
		case 0x10:	// small
			operands.add(memory.getByte(current.getPC()));
			current.setPC(current.getPC() + 1);
			break;
		case 0x20:	// variable (4.2.2)
			// Retrieve the variable number.
			int var = memory.getByte(current.getPC());
			current.setPC(current.getPC() + 1);
			// Retrieve the operand, stored in the variable.
			operands.add(current.getVariableValue(var));
			break;
		case 0x30: 	// omitted
		default:
			break;
		}

		// Create the opcode object.
		opcode = zmachine.getFactory().createOpcode(this);
		
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
	@Override
	public void exec() {
		opcode.exec();
	}

}
