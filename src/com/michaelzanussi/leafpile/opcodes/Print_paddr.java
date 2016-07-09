package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.zscii.V1ZSCII;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;
import com.michaelzanussi.leafpile.zscii.ZSCII;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Print_paddr (print string at paddr) instructions. See p. 92.
 * 
 * print_paddr packed-address-of-string
 * 
 * Print the (Z-encoded string) at the given packed address in high memory.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 July 2016) 
 */
public class Print_paddr extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Print_paddr(Instruction instruction) {
		super(instruction);
		name = "print_paddr";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int address = memory.unpackAddress(operands.get(0));
		
		ZSCII zscii = null;
		
		if (version < 3) {
			zscii = new V1ZSCII(memory);
		} else if (version < 5) {
			zscii = new V3ZSCII(memory);
		} else {
			assert(false) : "implement print_paddr for ver >= 5";
		}
		
		String string = zscii.decode(address);
				
		zmachine.ui().write(string);
		
		{
			System.out.println("PRINT_PADDR packed-address-of-string:" + address + " (string=~" + string + "~)");
			System.out.println();
		}
		
	}
	
}
