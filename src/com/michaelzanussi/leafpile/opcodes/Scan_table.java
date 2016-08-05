package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Scan_table (scan table) instructions. See p. 99.
 * 
 * scan_table x table len form -> (result)
 * 
 * Is x one of the words in table, which is len words long? If so, return
 * the address where it first occurs and branch. If not, return 0 and don't.
 * 
 * The form is optional (and only used in version 5?): bit 8 is set for 
 * words, clear for bytes: the rest contains the length of each field in
 * the table. (The first word or byte in each field being the one looked
 * at.) Thus $82 is the default.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (4 August 2016) 
 */
public class Scan_table extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Scan_table(Instruction instruction) {
		super(instruction);
		isStore = true;
		isBranch = true;
		name = "scan_table";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int x = operands.get(0);
		int table = operands.get(1);
		int len = operands.get(2);
		
		boolean found = false;
		
		// Try to locate x in the table.
		for (int i = 0; i < len; i++) {
			int value = memory.getWord(table);
			if (value == x) {
				found = true;
				break;
			} else {
				table += 2;
			}
		}
		
		{
			System.out.print("SCAN_TABLE x:" + x + " table:" + table + " len:" + len + " ");
			System.out.print("store:" + current.getStoreVariable() + " ");
			System.out.print("result:" + found + " ");
		}
		
		if (found) {
			current.setVariableValue(current.getStoreVariable(), table);			
			executeBranch(found);
		} else {
			current.setVariableValue(current.getStoreVariable(), 0);			
			executeBranch(found);
		}
		
		{
			System.out.println();
		}
		
	}
	
}
