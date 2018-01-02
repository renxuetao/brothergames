package com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.common.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Helper {
	// 检测网络连接
	public static boolean checkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
			return true;
		}
		return false;
	}

	/**
	 * 从网上获取内容get方式
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getStringFromUrl(String url) throws ClientProtocolException, IOException {
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

	public static String getStringFromUrlHeader(String url, String id, String sig, String token) throws ClientProtocolException, IOException {
		//		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Id", id);
		httppost.addHeader("Sig", sig);
		httppost.addHeader("Token", token);
		HttpResponse response = client.execute(httppost);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, "UTF-8");
	}
	
	private static AsyncHttpClient client = new AsyncHttpClient();    //实例话对象
	static {  
		client.setTimeout(5000);   //设置链接超时，如果不设置，默认为10s
	}
	//下载数据使用，会返回byte数据
	public static void get(String uString, BinaryHttpResponseHandler bHandler){
//		client.setEnableRedirects(false);
		client.getHttpClient().getParams().setParameter(ClientPNames.MAX_REDIRECTS, 3);
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(uString, bHandler);
	}

	public static void getJson(String ustring, JsonHttpResponseHandler bHandler){
		client.get(ustring, bHandler);
	}

	// 指定文件类型  
	public static String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };
	// 获取二进制数据如图片和其他文件  
	public static void getImage(String ustring){
		client.get(ustring, new BinaryHttpResponseHandler(allowedContentTypes) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] binaryData) {  
				String tempPath = Environment.getExternalStorageDirectory()
						.getPath() + "/temp.jpg";  
				Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
						binaryData.length);  

				File file = new File(tempPath);
				// 压缩格式  
				CompressFormat format = CompressFormat.JPEG;
				// 压缩比例  
				int quality = 100;  
				try {  
					// 若存在则删除  
					if (file.exists())  
						file.delete();  
					// 创建文件  
					file.createNewFile();  
					//  
					OutputStream stream = new FileOutputStream(file);
					// 压缩输出  
					bmp.compress(format, quality, stream);  
					// 关闭  
					stream.close();  
				} catch (IOException e) {
					// TODO Auto-generated catch block  
					e.printStackTrace();  
				}  

			}  

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] binaryData, Throwable error) {
			}  

			@Override
			public void onProgress(int bytesWritten, int totalSize) {  
				super.onProgress(bytesWritten, totalSize);  
			}  

		});  
	}

}
