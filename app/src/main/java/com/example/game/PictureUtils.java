package com.example.game;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

// This class will scale all of our image assets which will increase speed
public class PictureUtils {

    /* Method returns a scaled bitmap of our image asset
    *   inputs:
    *   -   Path: the path to our image
    *   -   destWidth: the specified width to be returned
    *   -   destHeight: the specified height to be returned
     */
    public static Bitmap getScaledBitmap(Resources resources,int id, int destWidth, int destHeight){
        // Get our image dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  // allows the caller to query the bitmap without having to allocated memory for its pixels
        BitmapFactory.decodeResource(resources, id);    // decodes the image into a bitmap

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Figure out how much to scale down by
        int inSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destWidth){
            float heightScale = srcHeight/destHeight;
            float widthScale = srcWidth/destWidth;

            // inSampleSize determines how big each "sample' should be for each pixel
            // a sample size of 1 has one final horizontal pixel for each horizontal pixel
            // in the original file, and a sample size of 2 has one horizontal pixel for every two
            // horizontal pixels in the original file. So when inSampleSize is 2, the image
            // has a quarter of the number of pixels of the original.
            inSampleSize = Math.round(Math.max(heightScale, widthScale));
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //create final bitmap
        return BitmapFactory.decodeResource(resources, id, options);
    }

    // Conservative scaling method to scale our bitmap
    public static Bitmap getScaledBitmap(Resources resources, int id, Activity activity){
        // get the size of our screen
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(resources, id, size.x, size.y);
    }
}
