package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Sound_effect (play sound effect) instructions. See p. 101.
 * 
 * sound_effect number effect volume routine
 * 
 * The given effect happens to the given sound number. The low byte of 
 * <bold>volume</bold> holds the volume level, the high byte the number
 * of repeats. (The value 255 means "loudest possible" and "forever"
 * respectively.) (In version 3, repeats are unsupported and the high
 * byte must be 0.)
 * 
 * Note that sound effect numbers 1 and 2 are bleeps (see S 9) and in
 * these cases the other operands must be omitted. Conversely, if any of
 * the other operands are present, the sound effect number must be 3 or
 * higher.
 * 
 * The effect can be: 1 (prepare), 2 (start), 3 (stop), 4 (finish with).
 * 
 * In Versions 5 and later, the routine is called (with no parameters)
 * after the sound has finished (it has been playing in the background
 * while the Z-machine has been working on other things.) (This is used
 * by 'Sherlock' to implement fading in and out, which explains why
 * mysterious numbers like $34FB were previously thought to be to do with
 * fading.) The routine is not called if the sound is stopped by another
 * sound or by an effect 3 call.
 * 
 * See the remarks to S 9 for which forms of this opcode were actually
 * used by Infocom.
 * 
 * In theory, @sound_effect; (with no operands at all) is illegal. However
 * interpreters are asked to beep (as if the operand were 1) if possible,
 * and in any case not to halt.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (10 August 2016) 
 */
public class Sound_effect extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Sound_effect(Instruction instruction) {
		super(instruction);
		name = "sound_effect";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
	
		// Retrieve the operands.
		int number;
		
		if (operands.size() > 1) {
			assert(false) : "hey! sound_effect found with >1 operands";
		}
		
		if (operands.size() == 1) {
			number = operands.get(0);
			if (number == 1) {
				zmachine.playSound(800, 50, 0.5);
			} else if (number == 2) {
				zmachine.playSound(200, 100, 1.0);
			} else {
				zmachine.playSound(800, 50, 0.5);
			}
		} else {
			return;
		}
		
		{
			System.out.println("SOUND_EFFECT number:" + number);
			System.out.println();
		}

	}
	
}
