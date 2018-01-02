package com.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

//import com.net.NetSrv;

public class Interaction implements Serializable {
	
	public ArrayList<String> filelist = new ArrayList<String>();

	public HashMap<String, String> hashmap = new HashMap<String, String>();
	public int newMessageCount = 0;
	public int notify_type = 0;//1 聊天
}
