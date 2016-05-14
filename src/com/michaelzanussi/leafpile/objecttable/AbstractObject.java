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
	protected int parent;
	protected int sibling;
	protected int child;
	
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
	
	public int getParent() {
		return parent;
	}
	
	public int getSibling() {
		return sibling;
	}
	
	public int getChild() {
		return child;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setProperty(int, int)
	 */
	public abstract void setProperty(int property, int value);
	
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
