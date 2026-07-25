package org.emby.androidtv.di

import org.emby.androidtv.util.ImageHelper
import org.koin.dsl.module

val utilsModule = module {
	single { ImageHelper(get()) }
}
