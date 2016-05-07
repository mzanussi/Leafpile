package com.michaelzanussi.leafpile.objecttable;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (27 April 2016) 
 */
public interface ObjectTableObject {

	public int getParent();
	public int getSibling();
	public int getChild();
	//private int getPropertyDefault(int prop_num);
}
