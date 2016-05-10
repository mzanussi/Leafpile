package com.michaelzanussi.leafpile.instructions;

import com.michaelzanussi.leafpile.factory.Factory;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (9 May 2016) 
 */
public class LongFormInstruction extends AbstractInstruction {

	public LongFormInstruction(Zmachine zmachine) {
		
		super(zmachine);
		
		// set the instruction form (4.3)
		form = Form.LONG;
		
		// operand count is 2OP. (4.3.2)
		opcount = Opcount.O_2OP;

		// Get the opcode. (4.3.3)
		opcode_no = (opcode_byte & 0x1f);

		// Bit 6 (0x40) of the opcode gives the type of the first operand.
		// 1 means a variable, 0 means a small constant. (4.4.2)
		if ((opcode_byte & 0x40) == 0x40) {
			// variable value
			int variable = memory.getByte(rous.getPC());
			rous.setPC(rous.getPC() + 1);
			int value = rous.getVariableValue(variable);
			operands.add(value);
		} else {
			// small constant (0-255)
			operands.add(memory.getByte(rous.getPC()));
			rous.setPC(rous.getPC() + 1);
		}
		
		// Bit 5 (0x20) of the opcode gives the type of the second operand.
		// 1 means a variable, 0 means a small constant. (4.4.2)
		if ((opcode_byte & 0x20) == 0x20) {
			// variable value
			int variable = memory.getByte(rous.getPC());
			rous.setPC(rous.getPC() + 1);
			int value = rous.getVariableValue(variable);
			operands.add(value);
		} else {
			// small constant (0-255)
			operands.add(memory.getByte(rous.getPC()));
			rous.setPC(rous.getPC() + 1);
		}
		
		// Create the opcode object.
		Factory factory = new Factory(zmachine);
		opcode = factory.createOpcode(this);
		
		// The variable number of where to put result. (4.6)
		if (opcode.isStore()) {
			int store = memory.getByte(rous.getPC());
			rous.setPC(rous.getPC() + 1);
			rous.setStore(store);
		}

		System.out.println();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.instructions.AbstractInstruction#exec()
	 */
	public void exec() {
		opcode.exec();
	}

}