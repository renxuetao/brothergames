package com.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;

import com.app.MyApplication;
import com.common.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Util {

	public static String encryptMD5(String strInput) {
		StringBuffer buf = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(strInput.getBytes("UTF-8"));
			byte b[] = md.digest();
			buf = new StringBuffer(b.length * 2);
			for (int i = 0; i < b.length; i++) {
				if ((b[i] & 0xff) < 0x10) {
					buf.append("0");
				}
				buf.append(Long.toHexString(b[i] & 0xff));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return buf.toString();
	}

	public static String encryptMD5(byte input[]) {
		StringBuffer buf = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input);
			byte b[] = md.digest();
			buf = new StringBuffer(b.length * 2);
			for (int i = 0; i < b.length; i++) {
				if ((b[i] & 0xff) < 0x10) {
					buf.append("0");
				}
				buf.append(Long.toHexString(b[i] & 0xff));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return buf.toString();
	}

	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
		try { 
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) { 
				// 获取网络连接管理的对象 
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) { 
					// 判断当前网络是否已经连接 
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true; 
					} 
				} 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false; 
	} 
	
	/**
	 * 从网上获取内容get方式
	 */
	@SuppressWarnings("deprecation")
	public static String getStringFromUrl(String url) throws ParseException, IOException {
		int REQUEST_TIMEOUT = 5*1000;//设置请求超时5秒钟  
		int SO_TIMEOUT = 50*1000;  //设置等待数据超时时间50秒钟  
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpGet get = new HttpGet(url);

		HttpEntity entity = null;
		try {
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == 200){
				entity = response.getEntity();
			}else{
//				return Constant.SERVER_ADDRESS_ERROR_TIPS;
			}
		} catch (Exception e) {
			e.printStackTrace();
//			return Constant.SERVER_ADDRESS_ERROR_TIPS;
		}
		return EntityUtils.toString(entity, "UTF-8");
	}

//	/**
//	 * 判断是否是手机号
//	 * @param phone
//	 * @return
//	 */
//	public static boolean phoneNumber(String phone, String restrict){
//		boolean b_number  = false;
//		if(phone.length() == 11){
//			phone = Constant.AREA_CODE+phone;
//		}
//		Pattern pat = Pattern.compile(restrict, Pattern.CASE_INSENSITIVE);
//		Matcher mat = pat.matcher(phone);
//		b_number = mat.find();
//		return b_number;
//	}

	//使用Bitmap加Matrix来缩放
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h){
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	private static long lastClickTime;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static String getWebSrv(){
		String ser_ip = MyApplication.getInstance().preferences.getString("WebSrv", "");
		int ser_port = MyApplication.getInstance().preferences.getInt("WebSrv_Port", 0);
		String webSrv = "http://" + ser_ip + ":" + ser_port;
		return webSrv;
	}

	/**
	 * 获取单个文件的MD5值！
	 * @param file
	 * @return
	 */

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return "";
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
	 * 安装apk
	 */
	public static void installAPK(Context context, File f){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static int getRandomInt(int max, int min){
		return (int) Math.round(Math.random() * (max - min) + min);
	}

	/**
	 * 解压缩zip包
	 * @param zipFile
	 * @param targetDir
	 * @throws IOException
	 */
	public static void unZip(String zipFile, String targetDir) throws IOException {
		int bufferLength = 4096;
		String strEntry;

		BufferedOutputStream dest = null;
		FileInputStream fis = new FileInputStream(zipFile);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry;

		delFile(new File(targetDir));

		while ((entry = zis.getNextEntry()) != null) {
			int count;
			byte data[] = new byte[bufferLength];
			strEntry = entry.getName();

			File entryFile = new File(targetDir + strEntry);
			File entryDir = new File(entryFile.getParent());
			if (!entryDir.exists()) {
				entryDir.mkdirs();
			}

			FileOutputStream fos = new FileOutputStream(entryFile);
			dest = new BufferedOutputStream(fos, bufferLength);
			while ((count = zis.read(data, 0, bufferLength)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
		}
		zis.close();
	}

	/**
	 * 删除文件
	 * @param file
	 */
	public static void delFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delFile(childFiles[i]);
			}
			file.delete();
		}
	}

	public static BitSet convert(long value) {
		BitSet bits = new BitSet();
		int index = 0;
		while (value != 0L) {
			if (value % 2L != 0) {
				bits.set(index);
			}
			++index;
			value = value >>> 1;
		}
		return bits;
	}

	public static long convert(BitSet bits) {
		long value = 0L;
		for (int i = 0; i < bits.length(); ++i) {
			value += bits.get(i) ? (1L << i) : 0L;
		}
		return value;
	}

	/**
	 * 判断是否为wifi
	 * @param inContext
	 * @return
	 */
	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
	 * @return int 得到的字符串长度
	 */
	public static int getLength(String s) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < s.length(); i++) {
			// 获取一个字符
			String temp = s.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 2;
			} else {
				// 其他字符长度为0.5
				valueLength += 1;
			}
		}
		//进位取整
		//return  Math.ceil(valueLength);
		return  valueLength;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(int dp,Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	// date类型转换为String类型
	// formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	// data Date类型的时间
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	// long类型转换为String类型
	// currentTime要转换的long类型的时间
	// formatType要转换的string类型的时间格式
	public static String longToString(long currentTime, String formatType) throws ParseException, java.text.ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	// string类型转换为date类型
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	public static Date stringToDate(String strTime, String formatType) throws ParseException, java.text.ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	// long转换为Date类型
	// currentTime要转换的long类型的时间
	// formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	public static Date longToDate(long currentTime, String formatType) throws ParseException, java.text.ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	// string类型转换为long类型
	// strTime要转换的String类型的时间
	// formatType时间格式
	// strTime的时间格式和formatType的时间格式必须相同
	public static long stringToLong(String strTime, String formatType) throws ParseException, java.text.ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	// date类型转换为long类型
	// date要转换的date类型的时间
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	//判断栈内是否有当前class
	public static boolean isInLauncher(Context mContext, Class c) {
		try {
			ActivityManager manager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
			String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
			if (name.equals(c.getName())) {
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	};

	public static short getAppVersion(Context context){
		String ver = "";
		short version = 0;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			ver = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if(ver.length() > 0){
			ver = ver.replaceAll("\\.", "");
			version = Short.parseShort(ver);
		}
		return version;
	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
								   double newHeight) {
		Bitmap bitmap = null;
		try {
			// 获取这个图片的宽和高
			float width = bgimage.getWidth();
			float height = bgimage.getHeight();
			LogUtil.d("width:" + width);
			LogUtil.d("height:" + height);
			// 创建操作图片用的matrix对象
			Matrix matrix = new Matrix();
			// 计算宽高缩放率
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 缩放图片动作
			matrix.postScale(scaleWidth, scaleHeight);
			bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
					(int) height, matrix, true);
		}catch (Exception e){
			e.printStackTrace();
		}

		return bitmap;
	}

	public static boolean isZhangYuePid(){
		if(TextUtils.equals(Constant.pid, "h_zy_smr")){
			return true;
		}
		return false;
	}

	public static boolean isXiaoMiPid(){
		if(TextUtils.equals(Constant.pid, "h_mi_jdts") || TextUtils.equals(Constant.pid, "h_mi_swpd")
				|| TextUtils.equals(Constant.pid, "h_mi_xymw")){
			return true;
		}
		return false;
	}

	public static boolean isHomePid(){
		if(TextUtils.equals(Constant.pid, "h_home_swpd") || TextUtils.equals(Constant.pid, "h_home_gs")){
			return true;
		}
		return false;
	}

	public static String mi_channels[] = new String[]{"h_mi_gs", "h_mi_yhgc", "h_mi_zzssxt", "h_mi_wxjw", "h_mi_csgl", "h_mi_xtjszj"};
	public static boolean isMiToPlatformPid(){
//		if(TextUtils.equals(Constant.pid, "h_mi_gs") || TextUtils.equals(Constant.pid, "h_mi_yhgc")
//				|| TextUtils.equals(Constant.pid, "h_mi_zzssxt")){
		for(int i = 0; i < mi_channels.length; i++){
			if(TextUtils.equals(Constant.pid, mi_channels[i])){
				return true;
			}
		}
		return false;
	}

	/**
	 * 对应的是不需要接入sdk,不需要登录的情况
	 * @return
     */
	public static boolean isSingleToPlatformPid(){
		if(TextUtils.equals(Constant.pid, "h_360_gs") || TextUtils.equals(Constant.pid, "h_360_swpd")){
			return true;
		}
		return false;
	}

	public static int convertAppVer(String ver){
		int version = 0;
		try{
			if(ver.length() > 0){
				ver = ver.replaceAll("\\.", "");
				version = Short.parseShort(ver);
			}
		}catch (Exception e){
			e.printStackTrace();
			version = 0;
		}
		return version;
	}

	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200*/
				LogUtil.d("此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.getClass().getName());
				if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					LogUtil.d("处于后台" + appProcess.processName);
					return true;
				} else {
					LogUtil.d("处于前台" + appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	public static String getPackageVersion(Activity activity) {
		String ver = "";
		try {
			PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			ver = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return ver;
	}
}
