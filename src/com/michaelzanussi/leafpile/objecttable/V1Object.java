package com.michaelzanussi.leafpile.objecttable;

import java.util.ArrayList;

import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zmachine.Zmachine;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;
import com.michaelzanussi.leafpile.zscii.ZSCII;

/**
 * A version 3 and earlier object.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (28 April 2016) 
 */
public class V1Object extends AbstractObject {

	private static final int PROPERTY_DEFAULTS_TABLE_SIZE = 31;
	private static final int MAX_OBJECTS = 255;
	private static final int OBJECT_SIZE = 9;
	private static final int ATTRIBUTES = 32;

	/**
	 * Constructor.
	 * 
	 * @param obj_num object number
	 * @param memory pointer to memory
	 */
	public V1Object(int obj_num, Zmachine zmachine /*Memory memory*/) {
		
		super(obj_num, zmachine/*memory*/);
		
		// If this is object 0, do nothing. (12.3)
		if (obj_num == 0) {
			return;
		}
		
		// Is this a valid object number? (12.3.1)
		if (obj_num < 0 || obj_num > MAX_OBJECTS) {
			throw new IndexOutOfBoundsException("Invalid object number: " + obj_num);
		}
		
		// Object table base address. (12.1)
		int obj_tbl_addr = memory.getObjectTableBase();
		// The offset into the object tree where we'll find our
		// object. For OBJECT_SIZE see 12.3.1.
		int offset = (obj_num - 1) * OBJECT_SIZE;
		// The absolute address of our object in the object tree.
		// It's the object table base address, plus the property
		// defaults table size, plus the offset. For the
		// PROPERTY_DEFAULTS_TABLE_SIZE see 12.2.
		int address = obj_tbl_addr + (PROPERTY_DEFAULTS_TABLE_SIZE * 2) + offset;

		// Attributes start object tree. (12.3.1)
		attributes = new ArrayList<Boolean>();
		
		for (int i = 0; i < ATTRIBUTES; i++) {
			int byteno = i / 8;
			int value = memory.getByte(address + byteno);
			boolean set = (value & (0x80 >> (i & 7))) != 0;
			attributes.add(set);
		}
		
		address += ATTRIBUTES / 8;
		
		// Parent object number after attributes. (12.3.1)
		parent = memory.getByte(address++);
		
		// Sibling object number after parent. (12.3.1)
		sibling = memory.getByte(address++);
		
		// Child object number after sibling. (12.3.1)
		child = memory.getByte(address++);
		
		// Finally, get the property table address. (12.3.1)
		address = memory.getWord(address);
		
		// First is the property table header, containing short name. (12.4)
		int text_length = memory.getByte(address++);
		switch (version) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			ZSCII zscii = new V3ZSCII(memory);
			short_name = zscii.decode(address);
		}
		address += text_length * 2;
		
		// Next are the properties for this object. (12.4.1)
		properties = new ArrayList<Property>();

		do {
			// Get the size byte. (12.4.1)
			int size_byte = memory.getByte(address++);
			
			if (size_byte == 0) {
				break;
			}
			
			// Get the property number and size The size byte is
			// arranged as 32 times the number of data bytes minus
			// one, plus the property number. (12.4.1)
			int prop_num = size_byte & 0x1f;
			int prop_size = (size_byte / 32) + 1;
			
			// Create the property and store it off in the list.
			Property property = new Property(prop_num, prop_size, address);
			properties.add(property);
			
			address += prop_size;
			
		} while (true);
		
	}
	
}
