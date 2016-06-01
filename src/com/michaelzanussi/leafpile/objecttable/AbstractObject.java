package com.michaelzanussi.leafpile.objecttable;

import java.util.List;

import com.michaelzanussi.leafpile.Debug;
import com.michaelzanussi.leafpile.zmachine.Memory;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (28 April 2016) 
 */
public abstract class AbstractObject implements ObjectTableObject {

	protected Memory memory;
	protected Debug debug;
	
	protected int version;
	
	protected int obj_num;
	protected List<Boolean> attributes;
	protected int parent;					// parent object
	protected int sibling;					// sibling object
	protected int child;					// child object
	
	protected int parent_addr;				// parent object address
	protected int sibling_addr;				// sibling object address
	protected int child_addr;				// child object address
	
	protected int prop_addr;
	// property table holds an array of Property objects
	protected List<Property> prop_table;
	protected int text_length;
	protected int short_name_ptr;
	protected String short_name;

	public AbstractObject(int obj_num, Memory memory) {
		this.obj_num = obj_num;
		this.memory = memory;
		debug = memory.getDebug();
		version = memory.getVersion();
	}
	
	@Override
	public int getParent() {
		return parent;
	}
	
	@Override
	public void setParent(int parent) {
		memory.setByte(parent_addr, parent);
	}
	
	@Override
	public int getSibling() {
		return sibling;
	}
	
	@Override
	public void setSibling(int sibling) {
		memory.setByte(sibling_addr, sibling);
	}
	
	@Override
	public int getChild() {
		return child;
	}
	
	@Override
	public void setChild(int child) {
		memory.setByte(child_addr, child);
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setProperty(int, int)
	 */
	@Override
	public abstract void setProperty(int property, int value);
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#isAttributeSet(int)
	 */
	@Override
	public boolean isAttributeSet(int attribute) {
		boolean isSet = attributes.get(attribute);
		return isSet;
	}
	
	/**
	 * The Property class represents a single property for an object.
	 * It holds the property number, the size of the property, and a
	 * pointer to the property data.
	 * 
	 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
	 * @version 1.0 (28 April 2016) 
	 *
	 */
	protected class Property {
		int prop_num;
		int prop_size;
		int prop_ptr;
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[" + prop_num + "] ");
			for (int i = 0; i < prop_size; i++) {
				sb.append(Integer.toHexString(memory.getByte(prop_ptr + i)) + " ");
			}
			sb.append("\n");
			return sb.toString();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(obj_num + ".\t");
		if (obj_num == 0) {
			sb.append("nothing");
			return sb.toString();
		}
		sb.append("Attributes: ");
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i)) {
				sb.append(i + " ");
			}
		}
		sb.append("\n");
		sb.append("\tParent object: " + parent + "  Sibling object: " + sibling + "  Child object: " + child + "\n");
		sb.append("\tProperty addresss: 0x" + Integer.toHexString(prop_addr) + "\n");
		sb.append("\t    Description: \"" + short_name + "\"\n");
		sb.append("\t    Properties: \n");
		for (Property property : prop_table) {
			sb.append("\t        " + property.toString());
		}
		return sb.toString();
	}

}
