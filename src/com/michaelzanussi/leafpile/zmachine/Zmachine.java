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
	
	// The story file.
	private File story;
	
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
	 * Set the story file.
	 * 
	 * @param story the story file.
	 */
	public void setStory(File story) {
		this.story = story;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		// do some init
		memory = new Memory(story);
		factory = new Factory(this);
		rscs = new ArrayDeque<Rous>();
		
		int version = memory.getVersion();
		
		// create new routine state with pc, set as current
		current = new Rous(memory.getInitialPC(), this);
		
		// setup header FLAGS1
		int flags1 = memory.getFlags1();
		if (version <= 3) {
			// status line NOT available? 1=true; 0=false
			flags1 &= 0xef;
			// screen split? 1=true; 0=false
			flags1 |= 0x20;
			// font is variable pitch font default? 1=true; 0=false
			flags1 &= 0xbf;
		} else {
			assert(false) : "set Flags1 for vers >= 4";
		}
		memory.setFlags1(flags1);
		
		while (true) {
			System.out.println("+++++ NEW INSTRUCTION +++++");
			if (current.getPC() == 28387) {	// debug only
				System.out.print("");
			}
			Instruction instruction = factory.createInstruction();
			System.out.println(instruction.toString());
			System.out.println("-- Now Executing --");
			instruction.exec();
		}
		
	}
	
}
