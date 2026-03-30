package com.orioninc.androidapptask

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import kotlinx.coroutines.Dispatchers

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val imageLoader =
            ImageLoader
                .Builder(this)
                .dispatcher(Dispatchers.IO.limitedParallelism(4))
                .crossfade(true)
                .memoryCache {
                    MemoryCache
                        .Builder(this)
                        .maxSizePercent(0.25)
                        .build()
                }.diskCache {
                    DiskCache
                        .Builder()
                        .directory(cacheDir.resolve("image_cache"))
                        .maxSizeBytes(50 * 1024 * 1024)
                        .build()
                }.build()

        Coil.setImageLoader(imageLoader)
    }
}
