package org.platkmframework.content.project;

import java.util.HashMap;

public class MapInfo extends HashMap<String,String>{

	private static final long serialVersionUID = 5327010767630664654L;

	public static MapInfo create() {
		return new MapInfo();
	}
	 
	public MapInfo add(String key, String value) {
		super.put(key, value);
		return this;
	}
}
