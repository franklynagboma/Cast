package com.androidtecknowlogy.tym.cast.helper.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by AGBOMA franklyn on 6/26/17.
 */

public class CircularTransform implements Transformation {

    public CircularTransform() {
    }

    @Override
    public Bitmap transform(Bitmap source) {

        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size,size);
        if(squared != source)
            source.recycle();

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squared,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float resize = size / 2f;
        canvas.drawCircle(resize, resize, resize, paint);

        squared.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "Circled";
    }


}