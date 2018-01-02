package com.app;

import android.os.Environment;

/**
 * xmpp配置页面
 */
public class XmppConnectionManager {
	//磁盘存储地址
	public static String PATH_BASE 								= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"brothergames";
	public static String SAVE_PATH 								= PATH_BASE+"/images";//设置保存图片文件的路径
	public static String VOICE_SAVE_PATH 						= PATH_BASE+"/voices";//设置保存语音文件的路径
	public static String VIDEO_SAVE_PATH 						= PATH_BASE+"/videos";//设置保存视频文件的路径
	public static String THUMBNAIL_SAVE_PATH 					= PATH_BASE+"/thumbnail";//设置保存视频文件缩略图的路径
	public static String DOWN_PATH 								= PATH_BASE+"/download/images";//下载图片
	public static String DOWNLOAD_APK 							= PATH_BASE+"/download_apk";
	public static String DOWNLOAD_RESOURCES 					= PATH_BASE+"/resources";//资源
	public static String SCREEN_TAKESHOOT 						= PATH_BASE+"/screen";//截屏
	public static String CRASH_PATH 							= PATH_BASE+"/crash";
	public static String updateDLDir 							= PATH_BASE+"/.update";
	public static String updateResource 						= updateDLDir + "/resource.zip";
	public static String updateApk 								= updateDLDir + "/brothergames.apk";
	public static String resourceDir 							= PATH_BASE+"/.resource/";
	public static String LODING_PATH 							= PATH_BASE+"/download/loding";

}
