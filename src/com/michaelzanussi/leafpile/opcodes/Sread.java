package com.michaelzanussi.leafpile.opcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.michaelzanussi.leafpile.dictionary.Dictionary;
import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * This class provides a concrete implementation of the <code>Opcode</code> 
 * interface for Sread (read from keyboard) instructions. See p. 94-96.
 * 
 * sread text parse time routine -> (result)
 * 
 * This opcode reads a whole command from the keyboard (no prompt is automatically
 * displayed). It is legal for this to be called with the cursor at any position
 * on any window.
 * 
 * In Versions 1 to 3, the status line is automatically redisplayed first.
 * 
 * A sequence of characters is read in from the current input stream until a 
 * carriage return (or, in Versions 5 and later, any terminating character is found.
 * 
 * In Versions 1 to 4, byte 0 of the text-buffer should initially contain the
 * maximum number of letters which can be typed, minus 1 (the interpreter should
 * not accept more than this). The text typed is reduced to lower case (so that it
 * can tidily be printed back by the program if need be) and stored in bytes 1
 * onward, with a zero terminator (but without any other terminator, such as a
 * carriage return code). (This means that if byte 0 contains n then the buffer
 * must contain n+1 bytes, which makes it a string array of length n in Inform
 * terminology.)
 * 
 * In Versions 5 and later, byte 0 of the text-buffer should initially contain the
 * maximum number of letters which can by typed (the interpreter should not accept
 * more than this). The interpreter stores the number of characters actually typed
 * in byte 1 (not counting the terminating character), and the characters themselves
 * in bytes 2 onward (not storing the terminating character). (Some interpreters
 * wrongly add a zero byte after the text anyway, so it is wise for the buffer to
 * contain at least n+3 bytes.)
 * 
 * Moreover, if byte 1 contains a positive value at the start of the input, then
 * read assumes that number of characters are left over from an interrupted
 * previous input, and writes the new characters after those already there. Note
 * that the interpreter does not redisplay the characters left over: the game does
 * this, if it wants to. This is unfortunate for any interpreter wanting to give
 * input text a distinctive appearance on-screen, but 'Beyond Zork', 'Zork Zero'
 * and 'Shogun' clearly require it.
 * 
 * In Version 4 and later, if the operands time and routine are supplied (and non-zero)
 * then the routine call routine() is made every time/10 seconds during the keyboard-
 * reading process. If this routine returns true, all input is erased (to zero) and
 * the reading process is terminated at once. (The terminating character code is 0.)
 * The routine is permitted to print to the screen even if it returns false to signal
 * "carry on": the interpreter should notice and redraw the input line so far, before
 * input continues. (Frotz notices by looking to see if the cursor position is at the
 * left-hand margin after the interrupt routine has returned.)
 * 
 * If input was terminated in the usual way, by the player typing a carriage return,
 * then a carriage return is printed (so the cursor moves to the next line). If it
 * was interrupted, the cursor is at the left at the rightmost end of the text typed
 * in so far.
 * 
 * Next, lexical analysis is performed on the text (except that in Versions 5 and 
 * later, if parse-buffer is zero then this is omitted). Initially, byte 0 of the
 * parse-buffer should hold the maximum number of textual words which can be parsed.
 * (If this is n, the buffer must be at least 2+4*n bytes long to hold the results
 * of the analysis.)
 * 
 * The interpreter divides the text into words and looks them up in the dictionary,
 * as described in S13. The number of words is written in byte 1 and one 4-byte
 * block is written for each word, from byte 2 onwards (except that it should stop
 * before going beyond the maximum number of words specified). Each block consists
 * of the byte address of the word in the dictionary, if it is in the dictionary, or
 * 0 if it isn't; followed by a byte giving the number of letters in the word; and
 * finally a byte giving the position in the text-buffer of the first letter of the
 * word.
 * 
 * In Version 5 and later, this is a store instruction: the return value is the
 * terminating character (note that the user pressing his "enter" key may cause
 * either 10 or 13 to be returned; the author recommends that interpreters return
 * 10). A timed-out input returns 0.
 * 
 * (Versions 1 and 2 and early Version 3 games mistakenly write the parse buffer
 * length 240 into byte 0 of the parse buffer: later games fix this bug and write 
 * 59, because 2+4*59 = 238 so that 59 is the maximum number of textual words which
 * can be parsed into a buffer of length 240 bytes. Old versions of the Inform 5
 * library commit the same error. Neither mistake has very serious consequences.)
 * 
 * (Interpreters are asked to halt with a suitable error message if the text or parse
 * buffers have length of less than 3 or 6 bytes, respectively: this sometimes occurs
 * due to a previous array being overrun, causing bugs which are very difficult to find.)
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (4 August 2016)
 */
public class Sread extends AbstractOpcode {

	/**
	 * Single-arg constructor takes Instruction object as only arg.
	 * 
	 * @param instruction the instruction
	 */
	public Sread(Instruction instruction) {
		super(instruction);
		name = "sread";
		// If Version 5 or later, this is a store instruction.
		if (version >= 5) {
			isStore = true;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.opcodes.AbstractOpcode#exec()
	 */
	@Override
	public void exec() {
		
		// Retrieve the operands.
		int text_buffer = operands.get(0);
		int parse_buffer = operands.get(1);
		
		// Additional operands.
		int time = 0;
		int routine = 0;
		
		// Flush the buffer.
		zmachine.ui().flush_buf();
		
		// Check version to determine if status line is re-displayed and
		// whether to get the two additional operands for V4+.
		if (version <= 3) {
			// TODO: redisplay status line
			assert(false);
		}
		else {
			if (operands.size() > 2) {
				time = operands.get(2);
			}
			if (operands.size() > 3) {
				routine = operands.get(3);
			}
		}
		
		{
			System.out.print("SREAD text:" + text_buffer + " parse:" + parse_buffer + " ");
			System.out.print("time:" + time + " routine:" + routine + " ");
			if (isStore) {
				System.out.print("store:" + current.getStoreVariable());
			}
			System.out.println();
		}
		
		if (time != 0 && routine != 0) {
			assert(false) : "complete timed read for versions >= 4";
		}
		
		if (version >= 5) {
			assert(false) : "complete read for versions >= 5";
		}

		// Retrieve the maximum input length.
		int max_inp_len = memory.getByte(text_buffer++);

		// Stores the user's input.
		StringBuilder input = new StringBuilder();

		// Get user input. TODO: display cursor!
		zmachine.ui().read(input, max_inp_len);

		{
			System.out.println("max_inp_len=" + max_inp_len);
			System.out.println("(user input of " + input.length() + " chars: ~~~" + input.toString() + "~~~)");
		}
		
		// User input stored in text-buffer byte 1 and forward.
		for (int i = 0; i < input.length(); i++) {
			memory.setByte(text_buffer++, input.charAt(i));
		}
		
		// Terminate with a null (0).
		memory.setByte(text_buffer, 0);
		
		// Lexical analysis starts now. Byte 0 holds the max 
		// number of textual words which can be parsed.
		int tw_to_parse = memory.getByte(parse_buffer++);
		
		{
			System.out.println("tw_to_parse=" + tw_to_parse);
		}
		
		// Tokenize the input TODO: NOTE, tokenise is already
		// defined by the standard, so this code which follows
		// will probably move to that opcode object. 
		
		StringTokenizer st = new StringTokenizer(input.toString(), " .,\"", true);
		List<String> tokens = new ArrayList<String>();
		
		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			if (!" ".equals(str)) {
				tokens.add(str);
			}
			// Check if we've reached max number of tokens.
			if (tokens.size() == tw_to_parse) {
				break;
			}
		}

		// # of words written in byte 1
		memory.setByte(parse_buffer++, tokens.size());
		
		// Create a dictionary to lookup words.
		Dictionary dictionary = zmachine.getFactory().createDictionary();
		
		// look each up in dictionary see S13
		// 1 4-byte block written for EACH word, from byte 2
		for (String token : tokens) {
			// 1: byte address of the word in the dict, 0 if not exist
			int addr = dictionary.lookup(token.length() > 6 ? token.substring(0, 6) : token);
			memory.setWord(parse_buffer, addr);
			parse_buffer += 2;
	 		// 2: byte giving the # of letters in word
			memory.setByte(parse_buffer++, token.length());
			// 3: byte giving pos in text-buffer of the first letter of word
			int loc = input.indexOf(token) + 1;
			memory.setByte(parse_buffer++, loc);
			{
				System.out.println("token: \"" + token + "\" addr:" + addr + " len:" + token.length() + " loc:" + loc);
			}
		}

		{
			System.out.println();
		}
	}
	
}
