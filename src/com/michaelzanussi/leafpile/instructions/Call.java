package com.michaelzanussi.leafpile.instructions;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public class Call extends AbstractOpcode {
	
	public Call(Instruction instruction/*Opcount opcount, int opcode_no, Zmachine zmachine*/) {
		super(instruction/*opcount, opcode_no, zmachine*/);
		isStore = true;
		name = "call";
	}
	
	public void exec(/*Instruction instruction*/) {
		System.out.println();
	}

}
