package com.example.qrhunter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
// https://stackoverflow.com/questions/5776851/load-image-from-url
import java.io.File;
import java.io.InputStream;
// this class is used to read image files from the web
public class ImageFile extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public ImageFile(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    /**
     * Read the image from a url
     * @param urls the urls to read
     * @return
     */
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
