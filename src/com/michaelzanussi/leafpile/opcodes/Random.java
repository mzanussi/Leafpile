package com.michaelzanussi.leafpile.opcodes;

import java.util.concurrent.ThreadLocalRandom;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Random (return a random number) instructions. See p. 94.
 * 
 * random range -> (result)
 * 
 * If range is positive, returns a uniformly random number between 1 and
 * range. If range is negative, the random number generator is seeded to
 * that value and the return value is 0. Most interpreters consider giving
 * 0 as a range illegal (because they attempt a division with remainder by
 * the range), but correct behaviour is to reseed the generator in as random
 * a way as the interpreter can (e.g. by using the time in milliseconds).
 * 
 * (Some version 3 games, such as 'Enchanter' release 29, had a debugging
 * verb #random such that typing, say, #random 14 caused a call of random
 * with 014.)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (8 August 2016)
 */
public class Random extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Random(Instruction instruction) {
		super(instruction);
		isStore = true;
		name = "random";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operand.
		int range = memory.signed(operands.get(0));
		int result = 0;
		
		// Get the random number, or set random generator seed.
		if (range > 0) {
			result = zmachine.getRandom(range);
			// TODO: ok, for testing purpose set to 63.
			result = 63;
		} else {
			if (range == 0) {
				zmachine.setSeed(System.currentTimeMillis());
			} else {
				zmachine.setSeed(range);				
			}
		}
		
		// Store off the result.
		current.setVariableValue(current.getStoreVariable(), result);
		
		{
			System.out.println("RANDOM range:" + range + " result:" + result + " store:" + current.getStoreVariable());
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
