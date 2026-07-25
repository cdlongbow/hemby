package org.emby.androidtv

import android.app.Application
import android.content.Context

class EmbyApplication : Application() {
	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		// ACRA crash reporting disabled - not supported by Emby server
	}
}
