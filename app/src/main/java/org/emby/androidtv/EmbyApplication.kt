package org.emby.androidtv

import android.app.Application
import android.content.Context
import org.emby.androidtv.telemetry.TelemetryService

class EmbyApplication : Application() {
	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		TelemetryService.init(this)
	}
}
