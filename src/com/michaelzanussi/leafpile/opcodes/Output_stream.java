package com.michaelzanussi.leafpile.opcodes;

import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Output_stream (output stream) instructions. See p. 90.
 * 
 * output_stream number table width
 * 
 * If stream is 0, nothing happens. If it is positive, then that stream is
 * selected; if negative, deselected. (Recall that several different streams
 * can be selected at once.)
 * 
 * When stream 3 is selected, a table must be given into which text can be
 * printed. The first word always holds the number of characters printed, the
 * actual text being stored at bytes table+2 onward. It is not the interpreter's
 * responsibility to worry about the length of this table being overrun.
 * 
 * In Version 6, a width field may optionally be given; if this is non-zero,
 * text will then be justified as if it were in the window with that number 
 * (if width is positive) or a box -width pixels wide (if negative). Then the
 * table will contain not ordinary text but formatted text: see print_form.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (3 Aug 2016) 
 */
public class Output_stream extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Output_stream(Instruction instruction) {
		super(instruction);
		name = "output_stream";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int number = memory.signed(operands.get(0));
		
		// Do nothing.
		if (number == 0) {
			System.out.println("OUTPUT_STREAM number:0");
			System.out.println();
			return;
		}
		
		// Stream is being deselected.
		if (number < 0) {
			zmachine.ui().setStream(-number, false);
			System.out.println("OUTPUT_STREAM number:" + number + " (deselected)");
			System.out.println();
			return;
		}
		
		// Select the stream.
		zmachine.ui().setStream(number, true);

		int table = 0;
		
		// If stream 3 is selected, get the table address.
		// Reset string length (1st byte of table address).
		if (number == 3) {
			table = operands.get(1);
			zmachine.ui().setStreamTableAddress(table);
			memory.setWord(table, 0);
			System.out.println("OUTPUT_STREAM number:" + number + " (selected) table:" + table);
			System.out.println();
			return;
		}
		
		{
			System.out.println("OUTPUT_STREAM number:" + number + " (selected)");
			System.out.println();
		}

	}
	
}
