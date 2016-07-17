package com.michaelzanussi.leafpile.factory;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import com.michaelzanussi.leafpile.instructions.Instruction;
import com.michaelzanussi.leafpile.instructions.Instruction.Opcount;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;
import com.michaelzanussi.leafpile.objecttable.V1Object;
import com.michaelzanussi.leafpile.objecttable.V4Object;
import com.michaelzanussi.leafpile.opcodes.Add;
import com.michaelzanussi.leafpile.opcodes.And;
import com.michaelzanussi.leafpile.opcodes.Buffer_mode;
import com.michaelzanussi.leafpile.opcodes.Call;
import com.michaelzanussi.leafpile.opcodes.Call_1s;
import com.michaelzanussi.leafpile.opcodes.Call_2s;
import com.michaelzanussi.leafpile.opcodes.Call_vs;
import com.michaelzanussi.leafpile.opcodes.Dec_chk;
import com.michaelzanussi.leafpile.opcodes.Div;
import com.michaelzanussi.leafpile.opcodes.Erase_window;
import com.michaelzanussi.leafpile.opcodes.Get_child;
import com.michaelzanussi.leafpile.opcodes.Get_prop;
import com.michaelzanussi.leafpile.opcodes.Get_sibling;
import com.michaelzanussi.leafpile.opcodes.Inc;
import com.michaelzanussi.leafpile.opcodes.Inc_chk;
import com.michaelzanussi.leafpile.opcodes.Insert_obj;
import com.michaelzanussi.leafpile.opcodes.Je;
import com.michaelzanussi.leafpile.opcodes.Jg;
import com.michaelzanussi.leafpile.opcodes.Jin;
import com.michaelzanussi.leafpile.opcodes.Jl;
import com.michaelzanussi.leafpile.opcodes.Jump;
import com.michaelzanussi.leafpile.opcodes.Jz;
import com.michaelzanussi.leafpile.opcodes.Loadb;
import com.michaelzanussi.leafpile.opcodes.Loadw;
import com.michaelzanussi.leafpile.opcodes.New_line;
import com.michaelzanussi.leafpile.opcodes.Opcode;
import com.michaelzanussi.leafpile.opcodes.Print;
import com.michaelzanussi.leafpile.opcodes.Print_char;
import com.michaelzanussi.leafpile.opcodes.Print_num;
import com.michaelzanussi.leafpile.opcodes.Print_obj;
import com.michaelzanussi.leafpile.opcodes.Print_paddr;
import com.michaelzanussi.leafpile.opcodes.Pull;
import com.michaelzanussi.leafpile.opcodes.Push;
import com.michaelzanussi.leafpile.opcodes.Put_prop;
import com.michaelzanussi.leafpile.opcodes.Read_char;
import com.michaelzanussi.leafpile.opcodes.Ret;
import com.michaelzanussi.leafpile.opcodes.Rtrue;
import com.michaelzanussi.leafpile.opcodes.Set_attr;
import com.michaelzanussi.leafpile.opcodes.Set_cursor;
import com.michaelzanussi.leafpile.opcodes.Set_text_style;
import com.michaelzanussi.leafpile.opcodes.Set_window;
import com.michaelzanussi.leafpile.opcodes.Split_window;
import com.michaelzanussi.leafpile.opcodes.Store;
import com.michaelzanussi.leafpile.opcodes.Storeb;
import com.michaelzanussi.leafpile.opcodes.Storew;
import com.michaelzanussi.leafpile.opcodes.Sub;
import com.michaelzanussi.leafpile.opcodes.Test_attr;
import com.michaelzanussi.leafpile.ui.components.Console;
import com.michaelzanussi.leafpile.ui.components.V1ScreenModel;
import com.michaelzanussi.leafpile.ui.components.V3ScreenModel;
import com.michaelzanussi.leafpile.ui.components.V4ScreenModel;
import com.michaelzanussi.leafpile.instructions.LongFormInstruction;
import com.michaelzanussi.leafpile.instructions.ShortFormInstruction;
import com.michaelzanussi.leafpile.instructions.VariableFormInstruction;
import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zmachine.Zmachine;
import com.michaelzanussi.leafpile.zscii.V1ZSCII;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;
import com.michaelzanussi.leafpile.zscii.ZSCII;

/**
 * A factory class for instantiating various kinds of objects.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (6 May 2016) 
 */
public class Factory {
	
	private Zmachine zmachine;
	private Memory memory;
	
	private int version;
	
	private Map<Integer, ObjectTableObject> object_table;
	
	/**
	 * Single-arg instruction takes a Z-machine as its only arg.
	 * 
	 * @param zmachine the Z-machine
	 */
	public Factory(Zmachine zmachine) {
		this.zmachine = zmachine;
		memory = zmachine.memory();
		version = memory.getVersion();
		object_table = new HashMap<Integer, ObjectTableObject>();
	}
	
	/**
	 * Returns a game console based on the story's screen model.
	 * 
	 * @param scr_width		screen width
	 * @param scr_height	screen height
	 * @param font			initial console font
	 * @return a game console 
	 */
	public Console createConsole(int scr_width, int scr_height, Font font) {
		if (version == 1 || version == 2) {
			return new V1ScreenModel(scr_width, scr_height, font);			
		} else if (version == 3) {
			return new V3ScreenModel(scr_width, scr_height, font);
		} else if (version == 4 || version ==5 ) {
			return new V4ScreenModel(scr_width, scr_height, font);
		} else {
			
		}
		return null;
	}
	
	public ZSCII createZSCII() {
		if (version < 3) {
			return new V1ZSCII(memory);
		}
		return new V3ZSCII(memory);
	}
	
	/**
	 * Retrieves an object table object for the given object number
	 * from the hashmap. If the object doesn't exist, create the object,
	 * add it to the hashmap, and return the newly created object.
	 * 
	 * @param obj_num the object number to retrieve
	 * @return the object table object
	 */
	public ObjectTableObject retrieveObject(int obj_num) {
		
		// Lookup the object in the hashmap.
		ObjectTableObject oto = object_table.get(obj_num);
		
		// If the object exists already, return it.
		if (oto != null) {
			System.out.println("existing object " + obj_num);
			return oto;
		}
		
		// Otherwise, create a new object, add it to the
		// hashmap, and then return the object.
		
		if (version < 4) {
			oto = new V1Object(obj_num, zmachine/*memory*/);
		} else {
			oto = new V4Object(obj_num, zmachine/*memory*/);
		}
		
		object_table.put(obj_num, oto);
		System.out.println("new object " + obj_num + " (object_table size=" + object_table.size() +")");
		
		return oto;
		
	}
	
	/**
	 * Creates an instruction (long, short, extended, or variable).
	 * 
	 * @return an instruction.
	 */
	public Instruction createInstruction() {
		// get the current routine's pc
		int pc = zmachine.getCurrentRous().getPC();
		// get the opcode byte
		int obyte = memory.getByte(pc);
		// get bits 6 & 7 to determine form (4.3)
		int bits67 = obyte >> 6;
		
		if (bits67 == 3) {				// variable form
			return new VariableFormInstruction(zmachine);
		} else if (obyte == 0xbe) {		// extended form
			assert (false) : "extended form";
		} else if (bits67 == 2) {		// short form
			return new ShortFormInstruction(zmachine);
		} else {						// long form
			return new LongFormInstruction(zmachine);
		}
		return null;
	}
	
	/**
	 * Creates an opcode object based on the operand count
	 * (0OP, 1OP, 2OP, or VAR) and the opcode number.
	 * 
	 * @param opcount the opcode count
	 * @param opcode the opcode number
	 * @return an opcode object
	 */
	public Opcode createOpcode(Instruction instruction) {
		
		Opcount opcount = instruction.getOpcount();
		int opcode_no = instruction.getOpcodeNumber();
		
		switch (opcount) {
		case O_0OP:
			switch (opcode_no) {
			case 0x00:
				return new Rtrue(instruction);
			case 0x02:
				return new Print(instruction);
			case 0x0b:
				return new New_line(instruction);
			default:
				assert (false) : "unimplemented 0OP opcode: 0x" + Integer.toHexString(opcode_no);
			}
			break;
		case O_1OP:
			switch (opcode_no) {
			case 0x00:
				return new Jz(instruction);
			case 0x01:
				return new Get_sibling(instruction);
			case 0x02:
				return new Get_child(instruction);
			case 0x05:
				return new Inc(instruction);
			case 0x08:
				return new Call_1s(instruction);
			case 0x0a:
				return new Print_obj(instruction);
			case 0x0b:
				return new Ret(instruction);
			case 0x0c:
				return new Jump(instruction);
			case 0x0d:
				return new Print_paddr(instruction);
			default:
				assert (false) : "unimplemented 1OP opcode: 0x" + Integer.toHexString(opcode_no);
			}
			break;
		case O_2OP:
			switch (opcode_no) {
			case 0x01:
				return new Je(instruction);
			case 0x02:
				return new Jl(instruction);
			case 0x03:
				return new Jg(instruction);
			case 0x04:
				return new Dec_chk(instruction);
//			case 0x05:
//				return new Inc_chk(instruction);
			case 0x06:
				return new Jin(instruction);
//			case 0x09:
//				return new And(instruction);
			case 0x0a:
				return new Test_attr(instruction);
			case 0x0b:
				return new Set_attr(instruction);
			case 0x0d:
				return new Store(instruction);
			case 0x0e:
				return new Insert_obj(instruction);
			case 0x0f:
				return new Loadw(instruction);
			case 0x10:
				return new Loadb(instruction);
			case 0x11:
				return new Get_prop(instruction);
			case 0x14:
				return new Add(instruction);
			case 0x15:
				return new Sub(instruction);
			case 0x17:
				return new Div(instruction);
			case 0x19:
				return new Call_2s(instruction);
			default:
				assert (false) : "unimplemented 2OP opcode: 0x" + Integer.toHexString(opcode_no);
			}
			break;
		case O_VAR:
			switch (opcode_no) {
			case 0x00:
				if (zmachine.memory().getVersion() <= 3) {
					return new Call(instruction);
				} else {
					return new Call_vs(instruction);
				}
			case 0x01:
				return new Storew(instruction);
			case 0x02:
				return new Storeb(instruction);
//			case 0x03:
//				return new Put_prop(instruction);
			case 0x05:
				return new Print_char(instruction);
//			case 0x06:
//				return new Print_num(instruction);
//			case 0x08:
//				return new Push(instruction);
//			case 0x09:
//				return new Pull(instruction);
			case 0x0a:
				return new Split_window(instruction);
			case 0x0b:
				return new Set_window(instruction);
			case 0x0d:
				return new Erase_window(instruction);
			case 0x0f:
				return new Set_cursor(instruction);
			case 0x11:
				return new Set_text_style(instruction);
			case 0x12:
				return new Buffer_mode(instruction);
			case 0x16:
				return new Read_char(instruction);
			default:
				assert (false) : "unimplemented VAR opcode: 0x" + Integer.toHexString(opcode_no);
			}
			break;
		}
		
		return null;
	}

}
