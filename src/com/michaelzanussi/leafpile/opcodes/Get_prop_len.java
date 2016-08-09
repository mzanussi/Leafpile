package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Get_prop_len (get length of property data) instructions. See p. 85.
 * 
 * get_prop_len property-address -> (result)
 * 
 * Get length of property data (in bytes) for the given object's property.
 * It is illegal to try to find the property length of a property which does
 * not exist for the given object, and an interpreter should halt with an
 * error message (if it can efficiently check this condition).
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 August 2016)
 */
public class Get_prop_len extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Get_prop_len(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "get_prop_len";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int property_address = operands.get(0);
		
		// Get the size byte.
        int size_byte = memory.getByte(property_address - 1);
		
		// Perform the operation and store.
		int result = 0;
		if (version <= 3) {
	        // Size byte is arranged as 32 times the number of
	        // data bytes minus 1, plus the property number.
	        result = ((size_byte >> 5) & 0x07) + 1;
		} else {
	        if ((size_byte & 0x80) == 0x80) {
	            // If the top bit is set, then there is a second size byte.
	        	// The bottom six bits contain the property data length
	        	// (counting in bytes). (12.4.2.1)
	        	result = (size_byte & 0x3f);
	        } else {
	        	// Otherwise, if bit 6 of the size byte is set then the
	        	// length is 2, and if it is clear then the length is 1.
	        	if ((size_byte & 0x40) == 0x40) {
	        		result = 2;
	        	} else {
	        		result = 1;
	        	}
	        }
		}
		current.setVariableValue(current.getStoreVariable(), result);
		
		{
			System.out.println("GET_PROP_LEN property-address:" + property_address + " result:" + result + " store:" + current.getStoreVariable());
			System.out.print("local vars now = ");
			int[] locals = current.getLocals();
			for (int i = 0; i < locals.length; i++) {
				System.out.print(locals[i] + " ");
			}
			System.out.print("\n");
			System.out.println("stack now = " + current.getStack());
			System.out.println();
		}
		
	}
	
}
