package com.michaelzanussi.leafpile.objecttable;

import java.util.List;

import com.michaelzanussi.leafpile.Debug;
import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This class provides a skeletal implementation of the <code>ObjectTableObject</code> 
 * interface, to minimize the effort required to implement this interface.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (28 April 2016) 
 */
public abstract class AbstractObject implements ObjectTableObject {

	protected Zmachine zmachine;
	protected Memory memory;
	protected Debug debug;
	
	protected int version;
	
	protected int obj_num;					// the object number
	protected List<Boolean> attributes;		// attributes (numbered starting at 0)
	protected int parent;					// parent object
	protected int sibling;					// sibling object
	protected int child;					// child object
	protected String short_name;			// object short name
	protected List<Property> properties;	// properties (numbered starting at 1)

	/**
	 * Constructor.
	 * 
	 * @param obj_num the object number
	 * @param memory pointer to memory
	 */
	public AbstractObject(int obj_num, Zmachine zmachine/*Memory memory*/) {
		this.obj_num = obj_num;
		this.zmachine = zmachine;
		memory = zmachine.memory();
		debug = memory.getDebug();
		version = memory.getVersion();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#getObjectNumber()
	 */
	@Override
	public int getObjectNumber() {
		return obj_num;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#getParent()
	 */
	@Override
	public int getParent() {
		return parent;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setParent(int)
	 */
	@Override
	public void setParent(int parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#getSibling()
	 */
	@Override
	public int getSibling() {
		return sibling;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setSibling(int)
	 */
	@Override
	public void setSibling(int sibling) {
		this.sibling = sibling;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#getChild()
	 */
	@Override
	public int getChild() {
		return child;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setChild(int)
	 */
	@Override
	public void setChild(int child) {
		this.child = child;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#isAttributeSet(int)
	 */
	@Override
	public boolean isAttributeSet(int attribute) {
		return attributes.get(attribute);
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setAttribute(int)
	 */
	@Override
	public void setAttribute(int attribute) {
		attributes.set(attribute, true);
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#getShortName()
	 */
	@Override
	public String getShortName() {
		return short_name;
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
		
		int number;		// property number
		int size;		// property size
		int address;	// address of property data
		
		/**
		 * Constructor.
		 * 
		 * @param number property number
		 * @param size property size
		 * @param address address of property data
		 */
		Property(int number, int size, int address) {
			this.number = number;
			this.size = size;
			this.address = address;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[" + number + "] ");
			for (int i = 0; i < size; i++) {
				sb.append(Integer.toHexString(memory.getByte(address + i)) + " ");
			}
			sb.append("\n");
			return sb.toString();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#getProperty(int)
	 */
	@Override
	public int getProperty(int property) {
		
		int address = 0;
		int size = 0;
		int data = 0;
		
		// Locate the property in the object. If found,
		// get the data address and size of the data.
		for (Property prop : properties) {
			if (prop.number == property) {
				address = prop.address;
				size = prop.size;
				break;
			}
		}
		
		// Check for the data address.
		if (address > 0) {
			// Get the data stored at address location.
			if (size == 1) {
				data = memory.getByte(address);
			} else {
				data = memory.getWord(address);
			}
		} else {
			// Gets called when the game attempts to read the value of property n
			// for an object which does not provide property n. In such a case,
			// the n-th entry in the property default table is the resulting value.
			int obj_tbl_addr = memory.getObjectTableBase();
			int offset = (property - 1) * 2;
			address = obj_tbl_addr + offset;
			data = memory.getWord(address);
		}
		
		return data;
		
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#setProperty(int, int)
	 */
	@Override
	public void setProperty(int property, int value) {
		assert(false) : "implement setProperty";
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.objecttable.ObjectTableObject#remove()
	 */
	@Override
	public void remove() {
		
		// Get the parent for this object.
		ObjectTableObject parent = zmachine.getFactory().retrieveObject(getParent());
		
		// Is this object the child of the parent?
		if (parent.getChild() == obj_num) {
			// So the parent's child is object we are going to
			// remove. It's the first child. So set the parent's
			// child to the object's sibling, and then reset
			// the object's parent and sibling.
			int sib = getSibling();
			parent.setChild(sib);
			setParent(0);
			setSibling(0);
			return;
		}
		
		// Starting with the parent's first child, walk thru
		// the sibling list until the object we want to remove
		// is located. Once located, the previous object
		// sibling should be our object. See 12.5 #b.
		
		// Verify parent has a child.
		if (parent.getChild() == 0) {
			throw new NullPointerException("Invalid object; parent has no child.");
		}
		
		ObjectTableObject previous = zmachine.getFactory().retrieveObject(parent.getChild());
		
		// Verify child has a sibling.
		if (previous.getSibling() == 0) {
			throw new NullPointerException("Invalid object; child has no sibling.");
		}
		
		ObjectTableObject current = zmachine.getFactory().retrieveObject(previous.getSibling());
		
		// Walk the tree...
		while (current.getObjectNumber() != getObjectNumber()) {
			previous = current;
			current = zmachine.getFactory().retrieveObject(previous.getSibling());
			if (current.getObjectNumber() == 0) {
				throw new NullPointerException("Invalid object; no sibling.");
			}
		}
		
		// Now remove the object, keeping children intact.
		previous.setSibling(getSibling());
		setParent(0);
		setSibling(0);
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
		sb.append("\t    Description: \"" + short_name + "\"\n");
		sb.append("\t    Properties: \n");
		for (Property property : properties) {
			sb.append("\t        " + property.toString());
		}
		return sb.toString();
	}

}
