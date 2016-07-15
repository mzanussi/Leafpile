package com.michaelzanussi.leafpile.objecttable;

/**
 * This interface defines an object contained in the object table.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (27 April 2016) 
 */
public interface ObjectTableObject {

	/**
	 * Returns the object's parent.
	 * 
	 * @return the object's parent
	 */
	public int getParent();
	
	/**
	 * Sets the object's parent.
	 * 
	 * @param parent the object's parent
	 */
	public void setParent(int parent);
	
	/**
	 * Returns the object's sibling.
	 * 
	 * @return the object's sibling
	 */
	public int getSibling();
	
	/**
	 * Sets the object's sibling.
	 * 
	 * @param sibling the object's sibling
	 */
	public void setSibling(int sibling);

	/**
	 * Returns the object's child.
	 * 
	 * @return the object's child
	 */
	public int getChild();
	
	/**
	 * Sets the object's child.
	 * 
	 * @param child the object's child
	 */
	public void setChild(int child);
	
	/**
	 * Returns true if the attribute is set, false if not.
	 * 
	 * @param attribute the attribute to check
	 * @return true if the attribute is set, false if not
	 */
	public boolean isAttributeSet(int attribute);
	
	/**
	 * Sets the given attribute.
	 * 
	 * @param attribute the attribute to set
	 */
	public void setAttribute(int attribute);
	
	/**
	 * Returns the object's short name.
	 * 
	 * @return the object's short name.
	 */
	public String getShortName();
	
	/**
	 * Returns the property's value.
	 * 
	 * @param property the property
	 * @return the property's value
	 */
	public int getProperty(int property);
	
	/**
	 * Sets the property's value.
	 * 
	 * @param property the property
	 * @param value the value
	 */
	public void setProperty(int property, int value);
	
}
