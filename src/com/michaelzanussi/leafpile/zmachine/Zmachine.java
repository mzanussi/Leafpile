package com.michaelzanussi.leafpile.zmachine;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;

import com.michaelzanussi.leafpile.factory.Factory;
import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.ui.IUI;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (5 May 2016) 
 */
public class Zmachine extends Thread {
	
	// The memory map, which is the story file.
	private Memory memory;
	
	// The story version.
	private int version;

	// Routine state call stack. So when a new routine is called,
	// the current routine state will be pushed onto the stack.
	// When the new routine completes, the old routine state will
	// be popped off the stack and execution continues where it
	// left off.
	private Deque<Rous> rscs;
	
	// The current routine being executed.
	private Rous current;
	
	private Factory factory;
	
	// The user interface.
	private IUI ui;
	
	public Zmachine(IUI ui) {
		this.ui = ui;
	}
	
	// getter
	public IUI ui() {
		return ui;
	}
	
	// getter
	public Memory memory() {
		return memory;
	}
	
	// getter
	public Rous getCurrentRous() {
		return current;
	}
	
	public Factory getFactory() {
		return factory;
	}
	
	public Rous createRoutine(int pc) {
		// first push the current routine onto the stack
		rscs.push(current);
		// then create a new routine and return it
		current = new Rous(pc, this);
		return current;
	}
	
	public Rous previousRoutine() {
		current = rscs.pop();
		return current;
	}
	
	/**
	 * Set the story file (i.e. initialize memory).
	 * 
	 * @param story the story file.
	 */
	public void setStory(File story) {
		// Initialize memory (i.e. open the story file).
		memory = new Memory(story);
		version = memory.getVersion();
		// Also create the Factory
		factory = new Factory(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		
		// do some init
		rscs = new ArrayDeque<Rous>();
		
		// create new routine state with pc, set as current
		current = new Rous(memory.getInitialPC(), this);
		
		// setup header FLAGS1
		int flags1 = memory.getFlags1();
		if (version <= 3) {
			if (ui.hasNoStatusLine()) {				// bit 4
				flags1 |= 0x10;
				// TODO: status line type
			}
			if (ui.hasSplitScreen()) {				// bit 5
				flags1 |= 0x20;
			}
			if (ui.isVariablePitchDefault()) {		// bit 6
				flags1 |= 0x40;
			}
		} else {
			// TODO: bit 0, bit 1, bit 5
			if (ui.isBoldAvailable()) {				// bit 2
				flags1 |= 0x04;
			}
			if (ui.isItalicAvailable()) {			// bit 3
				flags1 |= 0x08;
			}
			if (ui.isFixedSpaceFontAvailable()) {	// bit 4
				flags1 |= 0x10;
			}
			if (ui.hasTimedInput()) {				// bit 7
				flags1 |= 0x80;
			}
		}
		memory.setFlags1(flags1);
		
		// Set the interpreter number.
		memory.setInterpreterNumber(3);			// 3 = Mac (11.1.3)
		
		// Set the interpreter version.
		if (version == 4 || version == 5) {
			memory.setInterpreterVersion('Z');	// (11.1.3.1)
		} else if (version == 6) {
			memory.setInterpreterVersion(23);	// (11.1.3.1)
		}
		
		int counter=0;
				
		while (true) {
			counter++;
			System.out.println("+++++ NEW INSTRUCTION +++++ " + counter);
			if (current.getPC() == 66072) {	// debug only
				System.out.print("");
			}
			Instruction instruction = factory.createInstruction();
			System.out.println(instruction.toString());
			System.out.println("-- Now Executing --");
			instruction.exec();
		}
		
	}
	
}
