package org.platkmframework.content.ioc;

public class ObjectInfo {
	
	private String key;
	private Object obj;
	
	
	public ObjectInfo(String key, Object obj) {
		super();
		this.key = key;
		this.obj = obj;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
