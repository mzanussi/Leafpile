package com.michaelzanussi.leafpile.objecttable;

import java.util.ArrayList;

import com.michaelzanussi.leafpile.zmachine.Memory;
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
	public V1Object(int obj_num, Memory memory) {
		
		super(obj_num, memory);
		
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
		parent_addr = address++;
		parent = memory.getByte(parent_addr);
		
		// Sibling object number after parent. (12.3.1)
		sibling_addr = address++;
		sibling = memory.getByte(sibling_addr);
		
		// Child object number after sibling. (12.3.1)
		child_addr = address++;
		child = memory.getByte(child_addr);
		
		// Finally, get the property table address. (12.3.1)
		prop_addr = memory.getWord(address);
		
		// First is the property table header, containing short name. (12.4)
		// TODO: moved bulk of this code into ZSCII, consider using it here.
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
		
		// Next are the properties for this object. (12.4.1)
		prop_table = new ArrayList<Property>();

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
	@Override
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
	
	@Override
	public void setParent(int parent) {
		memory.setByte(parent_addr, parent);
	}
	
	@Override
	public void setSibling(int sibling) {
		memory.setByte(sibling_addr, sibling);
	}
	
	@Override
	public void setChild(int child) {
		memory.setByte(child_addr, child);
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
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setAttribute(int)
	 */
	@Override
	public void setAttribute(int attribute) {
		// See constructor above for details on these 3 vars.
		int obj_tbl_addr = memory.getObjectTableBase();
		int offset = (obj_num - 1) * OBJECT_SIZE;
		int address = obj_tbl_addr + (PROPERTY_DEFAULTS_TABLE_SIZE * 2) + offset;

		int byteno = attribute / 8;
		int value = memory.getByte(address + byteno);
		value |= (0x80 >> (attribute & 7));
		memory.setByte(address + byteno, value);
		attributes.set(attribute, true);
	}
	
}
