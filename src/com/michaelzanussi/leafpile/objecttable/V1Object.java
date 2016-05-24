package com.michaelzanussi.leafpile.objecttable;

import java.util.ArrayList;

import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;
import com.michaelzanussi.leafpile.zscii.ZSCII;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (28 April 2016) 
 */
public class V1Object extends AbstractObject {

	private static final int PROPERTY_DEFAULTS_TABLE_SIZE = 31;
	private static final int MAX_OBJECTS = 255;
	private static final int OBJECT_SIZE = 9;
	private static final int ATTRIBUTES = 32;

	public V1Object(int obj_num, Memory memory) {
		
		super(obj_num, memory);
		
		if (obj_num == 0) {
			return;
		}
		
		//{ debug.println("ZOBJECT: OBJ NUMBER " + obj_num); }
		
		if (obj_num < 1 || obj_num > MAX_OBJECTS) {
			throw new IndexOutOfBoundsException("Invalid object number: " + obj_num);
		}
		
		int obj_tbl_addr = memory.getObjectTableBase();
		int offset = (obj_num - 1) * OBJECT_SIZE;
		int address = obj_tbl_addr + (PROPERTY_DEFAULTS_TABLE_SIZE * 2) + offset;

		attributes = new ArrayList<Boolean>();
		
		for (int i = 0; i < ATTRIBUTES; i++) {
			int byteno = i / 8;
			int value = memory.getByte(address + byteno);
			boolean set = (value & (0x80 >> (i & 7))) != 0;
			attributes.add(set);
		}
		
		address += ATTRIBUTES / 8;
		
		//{ debug.print("parent: "); }
		parent_addr = address++;
		parent = memory.getByte(parent_addr/*address++*/);
		
		//{ debug.print("sibling: "); }
		sibling_addr = address++;
		sibling = memory.getByte(sibling_addr/*address++*/);
		
		//{ debug.print("child: "); }
		child_addr = address++;
		child = memory.getByte(child_addr/*address++*/);
		
		//{ debug.print("properties: "); }
		prop_addr = memory.getWord(address);
		
		prop_table = new ArrayList<Property>();
		
		address = prop_addr;
		text_length = memory.getByte(address++);
		assert(text_length>0) : "hmm text length is 0. troubleshoot.";
		int[] short_name_ints = new int[text_length];
		for (int i = 0; i < text_length; i++) {
			short_name_ints[i] = memory.getWord(address + (i * 2));
		}
		switch (version) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			ZSCII zscii = new V3ZSCII(memory);
			short_name = zscii.decode(short_name_ints);
		}
		address += text_length * 2;	// TODO: get short name, but skip for now.
		
		do {
			
			int size_byte = memory.getByte(address++);
			
			if (size_byte == 0) {
				break;
			}
			
			int prop_num = size_byte & 0x1f;
			int prop_size = (size_byte / 32) + 1;
			//System.out.println(prop_num + ": size=" + prop_size);
			Property property = new Property();
			property.prop_num = prop_num;
			property.prop_size = prop_size;
			property.prop_ptr = address;
			prop_table.add(property);
			
			address += prop_size;
			
		} while (true);
		
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.AbstractObject#setProperty(int, int)
	 */
	public void setProperty(int property, int value) {
		
		Property temp = null;
		
		for (Property prop : prop_table) {
			if (prop.prop_num == property) {
				temp = prop;
				break;
			}
		}
				
		if (temp == null) {
			throw new NullPointerException("No such property exists: " + property);
		}
		
		if (temp.prop_size == 1) {
			memory.setByte(temp.prop_ptr, value);
		} else {
			memory.setWord(temp.prop_ptr, value);
		}
		
	}
	
	// gets called when the game attempts to read the value of property n
	// for an object which does not provide property n. in such a case,
	// the n-th entry in the property default table is the resulting value.
	private int getPropertyDefault(int prop_num) {
		
		//{ debug.println("GET PROPERTY DEFAULT: PROPERTY " + prop_num); }
		
		// Properties are numbered 1 upwards to max allowable.
		if (prop_num < 1 || prop_num > PROPERTY_DEFAULTS_TABLE_SIZE) {
			return 0;
		}
		
		int obj_tbl_addr = memory.getObjectTableBase();
		
		int offset = (prop_num - 1) * 2;
		
		int address = obj_tbl_addr + offset;
		
		int dflt = memory.getWord(address);

		return dflt;
		
	}
	
}
