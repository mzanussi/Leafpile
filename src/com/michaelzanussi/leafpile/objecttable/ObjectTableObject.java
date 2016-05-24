package com.michaelzanussi.leafpile.objecttable;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (27 April 2016) 
 */
public interface ObjectTableObject {

	public int getParent();
	public int getSibling();
	public int getChild();
	
	public void setParent(int parent);
	// set the sibling on this object
	public void setSibling(int sibling);
	public void setChild(int child);
	
	//private int getPropertyDefault(int prop_num);
	public void setProperty(int property, int value);
	
	public boolean isAttributeSet(int attribute);
}
