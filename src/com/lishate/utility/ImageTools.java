package com.lishate.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageTools {
	public static final int LEFT = 0;

    public static final int RIGHT = 1;

    public static final int TOP = 3;

    public static final int BOTTOM = 4;

    /** */
    /**
     * 鍥剧墖鍘昏壊,杩斿洖鐏板害鍥剧墖
     * 
     * @param bmpOriginal 浼犲叆鐨勫浘鐗�
     * @return 鍘昏壊鍚庣殑鍥剧墖
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /** */
    /**
     * 鍘昏壊鍚屾椂鍔犲渾瑙�
     * 
     * @param bmpOriginal 鍘熷浘
     * @param pixels 鍦嗚寮у害
     * @return 淇敼鍚庣殑鍥剧墖
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /** */
    /**
     * 鎶婂浘鐗囧彉鎴愬渾瑙�
     * 
     * @param bitmap 闇�淇敼鐨勫浘鐗�
     * @param pixels 鍦嗚鐨勫姬搴�
     * @return 鍦嗚鍥剧墖
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /** */
    /**
     * 浣垮渾瑙掑姛鑳芥敮鎸丅itampDrawable
     * 
     * @param bitmapDrawable
     * @param pixels
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
        return bitmapDrawable;
    }

    /**
     * 璇诲彇璺緞涓殑鍥剧墖锛岀劧鍚庡皢鍏惰浆鍖栦负缂╂斁鍚庣殑bitmap
     * 
     * @param path
     */
    public static void saveBefore(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 鑾峰彇杩欎釜鍥剧墖鐨勫鍜岄珮
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 姝ゆ椂杩斿洖bm涓虹┖
        options.inJustDecodeBounds = false;
        // 璁＄畻缂╂斁姣�
        int be = (int)(options.outHeight / (float)200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = 2; // 鍥剧墖闀垮鍚勭缉灏忎簩鍒嗕箣涓�
        // 閲嶆柊璇诲叆鍥剧墖锛屾敞鎰忚繖娆¤鎶妎ptions.inJustDecodeBounds 璁句负 false鍝�
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        // savePNG_After(bitmap,path);
        saveJPGE_After(bitmap, path);
    }

    /**
     * 淇濆瓨鍥剧墖涓篜NG
     * 
     * @param bitmap
     * @param name
     */
    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 淇濆瓨鍥剧墖涓篔PEG
     * 
     * @param bitmap
     * @param path
     */
    public static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 姘村嵃
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 鍒涘缓涓�釜鏂扮殑鍜孲RC闀垮害瀹藉害涓�牱鐨勪綅鍥�
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 鍦�0锛�鍧愭爣寮�鐢诲叆src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 鍦╯rc鐨勫彸涓嬭鐢诲叆姘村嵃
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 淇濆瓨
        // store
        cv.restore();// 瀛樺偍
        return newb;
    }

    /**
     * 鍥剧墖鍚堟垚
     * 
     * @return
     */
    public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
        // newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
        }
        return newBitmap;
    }
    
    private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second, int direction) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }      
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;

        if (direction == LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (direction == TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);

        } else if (direction == BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
            
        }

        return newBitmap;
    }
    /**
     * 灏咮itmap杞崲鎴愭寚瀹氬ぇ灏�
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmapBySize(Bitmap bitmap,int width,int height)
    {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
    /**
     * Drawable 杞�Bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmapByBD(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * Bitmap 杞�Drawable
     * 
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * byte[] 杞�bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap 杞�byte[]
     * 
     * @param bm
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
