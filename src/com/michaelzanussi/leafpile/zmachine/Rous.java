package com.michaelzanussi.leafpile.zmachine;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A routine state, having nothing to do with rodents.
 * 
 * Each routine call, including the initial game routine,
 * will have its own routine state, which will be place
 * on the Z-machine stack. A description of this can be
 * found in "Overview of Z-machine Architecture" in the
 * standard.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (5 May 2016) 
 */
public class Rous {
	
	private Zmachine zmachine;
	
	private int pc;
	private int[] locals;	
	private int args;
	private Deque<Integer> stack;
	private int store;
	
	private static int _ctr = 0;
	private int id;
	
	public Rous(int pc, Zmachine zmachine) {
		this.pc = pc;
		this.zmachine = zmachine;
		locals = new int[15];
		args = 0;
		stack = new ArrayDeque<Integer>();
		store = 0;
		_ctr++;
		id = _ctr;
	}
	
	public int getId() {
		return id;
	}

	public int getPC() {
		return pc;
	}

	public void setPC(int pc) {
		this.pc = pc;
	}
	
	public int[] getLocals() {
		return locals;
	}
	
	public void setLocals(int[] locals) {
		this.locals = locals;
	}
	
	public int getNumberOfArgs() {
		return args;
	}
	
	public void setNumberOfArgs(int args) {
		this.args = args;
	}
	
	public Deque<Integer> getStack() {
		return stack;
	}
	
	public int getStoreVariable() {
		return store;
	}
	
	public void setStoreVariable(int store) {
		this.store = store;
	}
	
	/*public boolean isBranch() {
		return (branchWhenFalse || branchWhenTrue);
	}
	
	public boolean branchWhen() {
		if (branchWhenFalse) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setBranchWhenFalse(boolean branchWhenFalse) {
		this.branchWhenFalse = branchWhenFalse;
	}

	public void setBranchWhenTrue(boolean branchWhenTrue) {
		this.branchWhenTrue = branchWhenTrue;
	}
	
	public void setBranchOffset(int branchOffset) {
		this.branchOffset = branchOffset;
	}*/

	/**
	 * Returns the value for the specified variable. If variable is 0, get
	 * value from the top of the local stack; if variable is between 1 and 
	 * 15, get value from the local variable table; otherwise, get value 
	 * from the global variable table. (4.2.2)
	 * 
	 * @param variable
	 * @return
	 */
	public int getVariableValue(int variable) {
		if (variable == 0x00) {
			return stack.pop();
		} else if (variable <= 0x0f) {
			return locals[variable - 1];
		} else if (variable <= 0xff) {
			int globals = zmachine.memory().getGlobalVariablesTableBase();
			return zmachine.memory().getWord(globals + ((variable - 16) * 2));
		} else {
			throw new IndexOutOfBoundsException("No such variable: " + variable);
		}
	}
	
	/**
	 * Sets the value of the specified variable. If variable is 0, push the
	 * value onto the local stack; if variable is between 1 and 15, set value 
	 * on the local variable table; otherwise, set value in the global variable 
	 * table. (4.2.2)
	 * 
	 * @param variable
	 * @param value
	 */
	public void setVariableValue(int variable, int value) {
		// Test if this is void routine. If so, throw away result.
		if (variable < 0) {
			return;
		}
		if (variable == 0x00) {
			stack.push(value);
		} else if (variable <= 0x0f) {
			locals[variable - 1] = value;
		} else if (variable <= 0xff) {
			int globals = zmachine.memory().getGlobalVariablesTableBase();
			zmachine.memory().setWord(globals + ((variable - 16) * 2), value);
		} else {
			throw new IndexOutOfBoundsException("No such variable: " + variable);
		}
	}
}
