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
			parent_addr = address++;
			parent = memory.getByte(parent_addr/*address++*/);
		} else {
			parent_addr = address;
			address += 2;
			parent = memory.getWord(parent_addr/*address*/);
		}
		
		//{ debug.print("sibling: "); }
		if (version < 4) {
			sibling_addr = address++;
			sibling = memory.getByte(sibling_addr/*address++*/);
		} else {
			sibling_addr = address;
			address += 2;
			sibling = memory.getWord(sibling_addr/*address*/);
		}
		
		//{ debug.print("child: "); }
		if (version < 4) {
			child_addr = address++;
			child = memory.getByte(child_addr/*address++*/);
		} else {
			child_addr = address;
			address += 2;
			child = memory.getWord(child_addr/*address*/);
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

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.AbstractObject#setProperty(int, int)
	 */
	public void setProperty(int property, int value) {
		assert(false);
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
