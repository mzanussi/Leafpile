package com.michaelzanussi.leafpile.zmachine;

import java.io.File;
import java.util.Stack;

import com.michaelzanussi.leafpile.factory.Factory;
import com.michaelzanussi.leafpile.instructions.Instruction;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (5 May 2016) 
 */
public class Zmachine {
	
	// The memory map, which is the story file.
	private Memory memory;

	// Routine state call stack. So when a new routine is called,
	// the current routine state will be pushed onto the stack.
	// When the new routine completes, the old routine state will
	// be popped off the stack and execution continues where it
	// left off.
	private Stack<Rous> rscs;
	
	// The current routine being executed.
	private Rous rous;
	
	private Factory factory;
	
	public Zmachine(File story) {
		memory = new Memory(story);
		factory = new Factory(this);
		rscs = new Stack<Rous>();
	}
	
	// getter
	public Memory memory() {
		return memory;
	}
	
	// getter
	public Rous rous() {
		return rous;
	}
	
	public Rous createRoutine(int pc) {
		// first push the current routine onto the stack
		rscs.add(rous);
		// then create a new routine and return it
		rous = new Rous(pc, this);
		return rous;
	}
	
	public void test() {
		
		// create new routine state with pc, set as current
		rous = new Rous(memory.getInitialPC(), this);
		
		while (true) {
			Instruction instruction = factory.createInstruction();
			System.out.println(instruction);
			instruction.exec();
		}
		
	}
	
	public static void main(String[] args) {
		File file = new File("test/tdata.z3");
		Zmachine zmachine = new Zmachine(file);
		zmachine.test();
	}
	
}
