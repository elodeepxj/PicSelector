package com.jokerpeng.demo.picselector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by ${PengXiaoJie} on 2017/5/6.13 45..
 */

public class BitmapUtil {
    public BitmapUtil() {
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, int ratio) {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, ratio, var2);
        return var2.toByteArray();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int var3 = options.outWidth;
        int options1 = options.outHeight;
        int var4 = 1;
        if(var3 >= options1) {
            if(var3 > reqWidth || options1 > reqHeight) {
                options1 = Math.round((float)options1 / (float)reqHeight);
                reqWidth = Math.round((float)var3 / (float)reqWidth);
                var4 = options1 < reqWidth?options1:reqWidth;
            }
        } else if(var3 > reqHeight || options1 > reqWidth) {
            options1 = Math.round((float)options1 / (float)reqWidth);
            reqWidth = Math.round((float)var3 / (float)reqHeight);
            var4 = options1 < reqWidth?options1:reqWidth;
        }

        return var4;
    }

    public static String encodeBitmapData(Bitmap picBitmap) {
        try {
            byte[] var1;
            if((var1 = Bitmap2Bytes(picBitmap, 40)) != null) {
                String var6 = Base64.encodeToString(var1, 0);
                return var6;
            }

            return null;
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(picBitmap != null && !picBitmap.isRecycled()) {
                picBitmap.recycle();
            }

        }

        return null;
    }

    public static String encodeBitmapData(Bitmap picBitmap, int ratio) {
        if(ratio > 100) {
            ratio = 100;
        }

        if(ratio < 0) {
            ratio = 0;
        }

        try {
            byte[] ratio1;
            if((ratio1 = Bitmap2Bytes(picBitmap, ratio)) == null) {
                return null;
            }

            String ratio2 = Base64.encodeToString(ratio1, 0);
            return ratio2;
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if(picBitmap != null && !picBitmap.isRecycled()) {
                picBitmap.recycle();
            }

        }

        return null;
    }
}
