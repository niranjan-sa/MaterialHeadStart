package com.blogreader.niranjansa.materialheadstart.activity;

import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by vintej on 22/4/16.
 */
public class AlbumArt extends Service {
    public AlbumArt()
    {}
    public  Bitmap getAlbumArt(long albumId, Context c)
    {
        final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumId);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(c.getContentResolver(), albumArtUri);
        } catch (Exception exception) {
            Log.i("fffffff", "" + exception);
        }
        if(bitmap!=null)
        {
            bitmap = getResizedBitmap(bitmap, 100);

        }

        return bitmap;

    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        Bitmap bitmap= Bitmap.createScaledBitmap(image, width, height, true);
        if(width>height)
        {
            bitmap=Bitmap.createBitmap(bitmap, 0,0,height, height);
        }
        else
        {
            bitmap=Bitmap.createBitmap(bitmap, 0,0,width, width);

        }
        return bitmap;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
