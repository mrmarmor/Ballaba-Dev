package com.example.michaelkibenko.ballaba.Managers;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by User on 04/03/2018.
 */

public class ImageLoaderManager implements ImageLoader.ImageCache{
    public ImageLoader imageLoader;
    private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }

}
