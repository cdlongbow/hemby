package org.emby.androidtv.di

import org.emby.androidtv.preference.LiveTvPreferences
import org.emby.androidtv.preference.PreferencesRepository
import org.emby.androidtv.preference.SystemPreferences
import org.emby.androidtv.preference.TelemetryPreferences
import org.emby.androidtv.preference.UserPreferences
import org.emby.androidtv.preference.UserSettingPreferences
import org.koin.dsl.module

val preferenceModule = module {
	single { PreferencesRepository(get(), get(), get()) }

	single { LiveTvPreferences(get()) }
	single { UserSettingPreferences(get()) }
	single { UserPreferences(get()) }
	single { SystemPreferences(get()) }
	single { TelemetryPreferences(get()) }
}
