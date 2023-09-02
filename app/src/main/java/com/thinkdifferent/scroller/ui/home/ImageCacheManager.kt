package com.thinkdifferent.scroller.ui.home

import android.graphics.Bitmap
import android.util.LruCache

class ImageCacheManager {
    companion object {
        private val cache: LruCache<String, Bitmap>

        init {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            val cacheSize = maxMemory / 4 // Use 1/4th of the available memory for the cache

            cache = object : LruCache<String, Bitmap>(cacheSize) {
                override fun sizeOf(key: String, bitmap: Bitmap): Int {
                    // Return the size of the bitmap in kilobytes
                    return (bitmap.byteCount / 1024)
                }
            }
        }

        fun addBitmapToCache(key: String, bitmap: Bitmap) {
            if (getBitmapFromCache(key) == null) {
                cache.put(key, bitmap)
            }
        }

        fun getBitmapFromCache(key: String): Bitmap? {
            return cache.get(key)
        }
    }
}
