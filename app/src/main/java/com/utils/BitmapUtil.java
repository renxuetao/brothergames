package com.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.XmppConnectionManager;
import com.common.Constant;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by g on 2016/2/19.
 */
public class BitmapUtil {
    /**
     * 生成指定宽高的图片
     * @param path
     * @param w
     * @return
     */
    public static String createToWBitmap(String path, int w){
        try{
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int h = w*opts.outHeight/opts.outWidth;
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;

            // 获取缩放后图片
            Bitmap small = BitmapFactory.decodeFile(path, newOpts);

            File f = new File(XmppConnectionManager.SAVE_PATH);
            if(!f.exists()){
                f.mkdirs();
            }
            File myCaptureFile = new File(XmppConnectionManager.SAVE_PATH,  System.currentTimeMillis()+".jpg");
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            small.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            String mPath_small = myCaptureFile.getAbsolutePath();
            LogUtil.d("mPath_small:" + mPath_small);

            if(small != null && !small.isRecycled()){
                small.recycle();
                small = null;
            }
            return mPath_small;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
