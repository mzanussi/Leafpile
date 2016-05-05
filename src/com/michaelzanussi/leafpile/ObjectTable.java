package com.michaelzanussi.leafpile;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (27 April 2016) 
 */
public class ObjectTable {
	
	private Memory memory;
	private Map<Integer, ObjectTableObject> objectTree;
	
	public ObjectTable(Memory memory) {
		this.memory = memory;
		objectTree = new TreeMap<Integer, ObjectTableObject>();
	}
	
	public ObjectTableObject getObject(int obj_num) {
		ObjectTableObject oto = objectTree.get(obj_num);
		
		if (oto != null) {
			//System.out.println("Found it");
			return oto;
		}
		
		//System.out.println("No obj exists yet");
		if (memory.getVersion() < 4) {
			oto = new V1Object(obj_num, memory);
		} else {
			oto = new V4Object(obj_num, memory);
		}
		
		objectTree.put(obj_num, oto);
		
		return oto;
	}
	
}
