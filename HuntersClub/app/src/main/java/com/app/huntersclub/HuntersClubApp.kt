package com.app.huntersclub

import android.app.Application
import com.app.huntersclub.utils.DecoDrawableCache.preloadAllDecorations

class HuntersClubApp : Application() {

    override fun onCreate() {
        super.onCreate()

        preloadAllDecorations(this)
    }
}
