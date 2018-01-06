package com.bean;

import java.util.HashMap;
import java.util.Map;

public class MsgBean {
	private String type = "";
    private Map<String,Object> maps = null;

    public MsgBean(String type,Map<String,Object> maps) {
        this.type = type;
        this.maps = maps;
    }
    public MsgBean(String type) {
        this.type = type;
        maps = new HashMap<String, Object>();
    }
    public String getType(){
    	return type;
    }
    public Map<String,Object> getMaps(){
    	return maps;
    }
    public double getValueDouble(String key){
    	double value = 0;
    	if(maps != null){
    		try {
				value = Double.valueOf(maps.get(key).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				value = 0;
			}
    	}
    	return value;
    }

	public void setMsg(String key,Object value){
		if(maps != null){
			maps.put(key, value);
		}
	}
    
    public int getValueInteger(String key){
    	int value = 0;
    	if(maps != null){
    		try {
				value = Integer.valueOf(maps.get(key).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				value = 0;
			}
    	}
    	return value;
    } 
    public String getValueString(String key){
    	String value = "";
    	try {
    		value = maps.get(key).toString();
		} catch (Exception e) {
			e.printStackTrace();
			value = "";
		}
    	return value;
    } 
    
    public Object getValueObject(String key){
    	Object obj = null;
    	try {
    		obj = maps.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
		}
    	return obj;
    }
}
