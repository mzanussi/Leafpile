package com.michaelzanussi.leafpile.objecttable;

import java.util.ArrayList;

import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zmachine.Zmachine;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;
import com.michaelzanussi.leafpile.zscii.ZSCII;

/**
 * A version 4 or greater object.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (28 April 2016) 
 */
public class V4Object extends AbstractObject {

	private static final int PROPERTY_DEFAULTS_TABLE_SIZE = 63;
	private static final int MAX_OBJECTS = 65535;
	private static final int OBJECT_SIZE = 14;
	private static final int ATTRIBUTES = 48;

	/**
	 * Constructor.
	 * 
	 * @param obj_num object number
	 * @param memory pointer to memory
	 */
	public V4Object(int obj_num, Zmachine zmachine/*Memory memory*/) {
		
		super(obj_num, zmachine/*memory*/);
		
		// If this is object 0, do nothing. (12.3)
		if (obj_num == 0) {
			return;
		}

		// Is this a valid object number? (12.3.2)
		if (obj_num < 0 || obj_num > MAX_OBJECTS) {
			throw new IndexOutOfBoundsException("Invalid object number: " + obj_num);
		}
		
		// Object table base address. (12.1)
		int obj_tbl_addr = memory.getObjectTableBase();
		// The offset into the object tree where we'll find our
		// object. For OBJECT_SIZE see 12.3.2.
		int offset = (obj_num - 1) * OBJECT_SIZE;
		// The absolute address of our object in the object tree.
		// It's the object table base address, plus the property
		// defaults table size, plus the offset. For the
		// PROPERTY_DEFAULTS_TABLE_SIZE see 12.2.
		int address = obj_tbl_addr + (PROPERTY_DEFAULTS_TABLE_SIZE * 2) + offset;

		// Attributes start object tree. (12.3.2)
		attributes = new ArrayList<Boolean>();
		
		for (int i = 0; i < ATTRIBUTES; i++) {
			int byteno = i / 8;
			int value = memory.getByte(address + byteno);
			boolean set = (value & (0x80 >> (i & 7))) != 0;
			attributes.add(set);
		}
		
		address += ATTRIBUTES / 8;
		
		// Parent object number after attributes. (12.3.2)
		parent = memory.getWord(address);
		address += 2;
		
		// Sibling object number after parent. (12.3.2)
		sibling = memory.getWord(address);
		address += 2;
		
		// Child object number after sibling. (12.3.2)
		child = memory.getWord(address);
		address += 2;
		
		// Finally, get the property table address. (12.3.2)
		address = memory.getWord(address);
		
		// First is the property table header, containing short name. (12.4)
		int text_length = memory.getByte(address++);
		switch (version) {
		case 4:
			ZSCII zscii = new V3ZSCII(memory);
			short_name = zscii.decode(address);
			break;
		default:
			assert(false) : "add zscii support >= 5";
		}
		address += text_length * 2;
		
		// Next are the properties for this object. (12.4.2)
		properties = new ArrayList<Property>();
		
		do {
			// Get the size byte. (12.4.2)
			int size_byte = memory.getByte(address++);
			
			if (size_byte == 0) {
				break;
			}
			
			// Get the property number. It's the bottom six
			// bits of the size byte. (12.4.2)
			int prop_num = size_byte & 0x3f;
			
			// Get the property size. Check for a 2nd size byte. 
			int prop_size = 0;
			if ((size_byte & 0x80) == 0x80) {
				// Top bit is set, there is a 2nd size byte. The
				// bottom six bits is the property data length 
				// (counting in bytes), and if 0, this should be
				// interpreted as length of 64. (12.4.2.1, 12.4.2.1.1)
				int size_byte2 = memory.getByte(address++);
				int bottom6 = size_byte2 & 0x3f;
				if (bottom6 == 0) {
					prop_size = 64;
				} else {
					prop_size = bottom6;
				}
			} else {
				// If bit 6 of the size byte is set then the
				// length is 2, and if it is clear, then the
				// length is 1. (12.4.2.2)
				if ((size_byte & 0x40) == 0x40) {
					prop_size = 2;
				} else {
					prop_size = 1;				
				}
			}
			
			// Create the property and store it off in the list.
			Property property = new Property(prop_num, prop_size, address);
			properties.add(property);
			
			address += prop_size;
			
		} while (true);
		
	}

}
