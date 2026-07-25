package org.emby.androidtv.di

import org.emby.androidtv.auth.repository.AuthenticationRepository
import org.emby.androidtv.auth.repository.AuthenticationRepositoryImpl
import org.emby.androidtv.auth.repository.ServerRepository
import org.emby.androidtv.auth.repository.ServerRepositoryImpl
import org.emby.androidtv.auth.repository.ServerUserRepository
import org.emby.androidtv.auth.repository.ServerUserRepositoryImpl
import org.emby.androidtv.auth.repository.SessionRepository
import org.emby.androidtv.auth.repository.SessionRepositoryImpl
import org.emby.androidtv.auth.store.AuthenticationPreferences
import org.emby.androidtv.auth.store.AuthenticationStore
import org.koin.dsl.module

val authModule = module {
	single { AuthenticationStore(get()) }
	single { AuthenticationPreferences(get()) }

	single<AuthenticationRepository> {
		AuthenticationRepositoryImpl(get(), get(), get(), get(), get(), get(defaultDeviceInfo))
	}
	single<ServerRepository> { ServerRepositoryImpl(get(), get()) }
	single<ServerUserRepository> { ServerUserRepositoryImpl(get(), get()) }
	single<SessionRepository> {
		SessionRepositoryImpl(get(), get(), get(), get(), get(defaultDeviceInfo), get(), get(), get())
	}

	factory {
		val serverRepository = get<ServerRepository>()
		serverRepository.currentServer.value?.serverVersion ?: ServerRepository.minimumServerVersion
	}
}
