package com.michaelzanussi.leafpile.objecttable;

import java.util.ArrayList;

import com.michaelzanussi.leafpile.zmachine.Memory;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (28 April 2016) 
 */
public class V4Object extends AbstractObject {

	private static final int PROPERTY_DEFAULTS_TABLE_SIZE = 63;
	private static final int MAX_OBJECTS = 65535;
	private static final int OBJECT_SIZE = 14;
	private static final int ATTRIBUTES = 48;

	public V4Object(int obj_num, Memory memory) {
		
		super(obj_num, memory);
		
		if (obj_num == 0) {
			return;
		}

		//int max_objects = (version < 4 ? MAX_OBJECTS_V1 : MAX_OBJECTS_V4);
		//int object_size = (version < 4 ? OBJECT_SIZE_V1 : OBJECT_SIZE_V4);
		//int prop_dflt_tbl_size = (version < 4 ? PDT_V1 : PDT_V4) * 2;
		
		if (obj_num < 1 || obj_num > MAX_OBJECTS) {
			throw new IndexOutOfBoundsException("Invalid object number: " + obj_num);
		}
		
		int obj_tbl_addr = memory.getObjectTableBase();
		int offset = (obj_num - 1) * OBJECT_SIZE;
		int address = obj_tbl_addr + (PROPERTY_DEFAULTS_TABLE_SIZE * 2) + offset;

		//int attr_count = (version < 4 ? ATTRIBUTES_V1 : ATTRIBUTES_V4 );
		attributes = new ArrayList<Boolean>();
		
		for (int i = 0; i < ATTRIBUTES; i++) {
			int byteno = i / 8;
			int value = memory.getByte(address + byteno);
			boolean set = (value & (0x80 >> (i & 7))) != 0;
			attributes.add(set);
		}
		
		address += ATTRIBUTES / 8;
		//{ debug.println("attributes: " + attributes); }
		
		//{ debug.print("parent: "); }
		if (version < 4) { 
			parent = memory.getByte(address++);
		} else {
			parent = memory.getWord(address);
			address += 2;
		}
		
		//{ debug.print("sibling: "); }
		if (version < 4) {
			sibling = memory.getByte(address++);
		} else {
			sibling = memory.getWord(address);
			address += 2;
		}
		
		//{ debug.print("child: "); }
		if (version < 4) {
			child = memory.getByte(address++);
		} else {
			child = memory.getWord(address);
			address += 2;
		}
		
		//{ debug.print("properties: "); }
		prop_addr = memory.getWord(address);
		
		prop_table = new ArrayList<Property>();
		
		// TODO: add support for versions >= 4
		address = prop_addr;
		int text_length = memory.getByte(address++);
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
