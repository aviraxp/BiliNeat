package com.iacn.bilineat.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by iAcn on 2017/1/12
 * Emali iAcn0301@foxmail.com
 */

public class ImgUtils {

    public static void saveDrawableToLocal(Drawable drawable){
        Bitmap bitmap = drawableToBitmap(drawable);
        FileOutputStream fos = null;

        try {
            File file = new File(Environment.DIRECTORY_PICTURES + "BiliNeat/aaa.jpg");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } finally {
            try {
                if (fos != null) {
                    // 因为 Bitmap.compress 方法是异步保存
                    // 所以这里使用 flush 手动刷写一下
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(width, height, config);

        // 手动绘制一下，不然系统在某些情况下绘制 View缓存的话会出不来
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);

        return bitmap;
    }
}