package org.emby.androidtv.di

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.network.NetworkFetcher
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.serviceLoaderEnabled
import coil3.svg.SvgDecoder
import coil3.util.Logger
import org.emby.androidtv.BuildConfig
import org.emby.androidtv.auth.repository.ServerRepository
import org.emby.androidtv.auth.repository.UserRepository
import org.emby.androidtv.auth.repository.UserRepositoryImpl
import org.emby.androidtv.data.eventhandling.SocketHandler
import org.emby.androidtv.data.model.DataRefreshService
import org.emby.androidtv.data.repository.CustomMessageRepository
import org.emby.androidtv.data.repository.CustomMessageRepositoryImpl
import org.emby.androidtv.data.repository.ExternalAppRepository
import org.emby.androidtv.data.repository.ItemMutationRepository
import org.emby.androidtv.data.repository.ItemMutationRepositoryImpl
import org.emby.androidtv.data.repository.NotificationsRepository
import org.emby.androidtv.data.repository.NotificationsRepositoryImpl
import org.emby.androidtv.data.repository.UserViewsRepository
import org.emby.androidtv.data.repository.UserViewsRepositoryImpl
import org.emby.androidtv.data.service.BackgroundService
import org.emby.androidtv.integration.dream.DreamViewModel
import org.emby.androidtv.ui.InteractionTrackerViewModel
import org.emby.androidtv.ui.itemhandling.ItemLauncher
import org.emby.androidtv.ui.navigation.Destinations
import org.emby.androidtv.ui.navigation.NavigationRepository
import org.emby.androidtv.ui.navigation.NavigationRepositoryImpl
import org.emby.androidtv.ui.playback.PlaybackControllerContainer
import org.emby.androidtv.ui.playback.external.DefaultExternalPlayerApi
import org.emby.androidtv.ui.playback.external.ExternalPlayerApi
import org.emby.androidtv.ui.playback.external.MpvExternalPlayerApi
import org.emby.androidtv.ui.playback.external.MxExternalPlayerApi
import org.emby.androidtv.ui.playback.external.VimuExternalPlayerApi
import org.emby.androidtv.ui.playback.external.VlcExternalPlayerApi
import org.emby.androidtv.ui.playback.nextup.NextUpViewModel
import org.emby.androidtv.ui.playback.segment.MediaSegmentRepository
import org.emby.androidtv.ui.playback.segment.MediaSegmentRepositoryImpl
import org.emby.androidtv.ui.playback.stillwatching.StillWatchingViewModel
import org.emby.androidtv.ui.player.photo.PhotoPlayerViewModel
import org.emby.androidtv.ui.search.SearchFragmentDelegate
import org.emby.androidtv.ui.search.SearchRepository
import org.emby.androidtv.ui.search.SearchRepositoryImpl
import org.emby.androidtv.ui.search.SearchViewModel
import org.emby.androidtv.ui.settings.compat.SettingsViewModel
import org.emby.androidtv.ui.startup.ServerAddViewModel
import org.emby.androidtv.ui.startup.StartupViewModel
import org.emby.androidtv.ui.startup.UserLoginViewModel
import org.emby.androidtv.util.AndroidVersion
import org.emby.androidtv.util.KeyProcessor
import org.emby.androidtv.util.MarkdownRenderer
import org.emby.androidtv.util.PlaybackHelper
import org.emby.androidtv.util.apiclient.ReportingHelper
import org.emby.androidtv.util.coil.CoilTimberLogger
import org.emby.androidtv.util.coil.createCoilConnectivityChecker
import org.emby.androidtv.util.apiclient.EmbyUrlInterceptor
import org.emby.androidtv.util.sdk.SdkPlaybackHelper
import org.jellyfin.sdk.android.androidDevice
import org.jellyfin.sdk.api.client.HttpClientOptions
import org.jellyfin.sdk.api.okhttp.OkHttpFactory
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.jellyfin.sdk.Jellyfin as JellyfinSdk

val defaultDeviceInfo = named("defaultDeviceInfo")

val appModule = module {
	// SDK
	single(defaultDeviceInfo) { androidDevice(get()) }
	single {
		val client = okhttp3.OkHttpClient.Builder()
			.addInterceptor(EmbyUrlInterceptor())
			.build()
		OkHttpFactory(base = client)
	}
	single { HttpClientOptions() }
	single {
		createJellyfin {
			context = androidContext()

			// Add client info
			val clientName = buildString {
				append("hemby")
				if (BuildConfig.DEBUG) append(" (debug)")
			}
			clientInfo = ClientInfo(clientName, BuildConfig.VERSION_NAME)
			deviceInfo = get(defaultDeviceInfo)

			// Change server version
			minimumServerVersion = ServerRepository.minimumServerVersion

			// Use our own shared factory instance
			apiClientFactory = get<OkHttpFactory>()
			socketConnectionFactory = get<OkHttpFactory>()
		}
	}

	single {
		// Create an empty API instance, the actual values are set by the SessionRepository
		get<JellyfinSdk>().createApi(httpClientOptions = get<HttpClientOptions>())
	}

	single { SocketHandler(get(), get(), get(), get(), get(), get(), get(), get(), get(), ProcessLifecycleOwner.get().lifecycle) }

	// Coil (images)
	single {
		val okHttpFactory = get<OkHttpFactory>()
		val httpClientOptions = get<HttpClientOptions>()

		@OptIn(ExperimentalCoilApi::class)
		OkHttpNetworkFetcherFactory(
			callFactory = { okHttpFactory.createClient(httpClientOptions) },
			connectivityChecker = ::createCoilConnectivityChecker,
		)
	}

	single {
		ImageLoader.Builder(androidContext()).apply {
			serviceLoaderEnabled(false)
			logger(CoilTimberLogger(if (BuildConfig.DEBUG) Logger.Level.Warn else Logger.Level.Error))

			components {
				add(get<NetworkFetcher.Factory>())

				if (AndroidVersion.isAtLeastP) add(AnimatedImageDecoder.Factory())
				else add(GifDecoder.Factory())
				add(SvgDecoder.Factory())
			}
		}.build()
	}

	// Non API related
	single { DataRefreshService() }
	single { PlaybackControllerContainer() }
	single { InteractionTrackerViewModel(get(), get()) }

	single<UserRepository> { UserRepositoryImpl() }
	single<UserViewsRepository> { UserViewsRepositoryImpl(get()) }
	single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }
	single<ItemMutationRepository> { ItemMutationRepositoryImpl(get(), get()) }
	single<CustomMessageRepository> { CustomMessageRepositoryImpl() }
	single<NavigationRepository> { NavigationRepositoryImpl(Destinations.home) }
	single<SearchRepository> { SearchRepositoryImpl(get()) }
	single<MediaSegmentRepository> { MediaSegmentRepositoryImpl(get(), get()) }
	single<ExternalAppRepository> { ExternalAppRepository(get(), getAll(), get<DefaultExternalPlayerApi>()) }

	// External player APIs
	single { VlcExternalPlayerApi() } bind ExternalPlayerApi::class
	single { MxExternalPlayerApi() } bind ExternalPlayerApi::class
	single { MpvExternalPlayerApi() } bind ExternalPlayerApi::class
	single { VimuExternalPlayerApi() } bind ExternalPlayerApi::class
	single { DefaultExternalPlayerApi() }

	viewModel { StartupViewModel(get(), get(), get(), get()) }
	viewModel { UserLoginViewModel(get(), get(), get(), get(defaultDeviceInfo)) }
	viewModel { ServerAddViewModel(get()) }
	viewModel { NextUpViewModel(get(), get(), get()) }
	viewModel { StillWatchingViewModel(get(), get(), get(), get()) }
	viewModel { PhotoPlayerViewModel(get()) }
	viewModel { SearchViewModel(get()) }
	viewModel { DreamViewModel(get(), get(), get(), get(), get()) }
	viewModel { SettingsViewModel() }

	single { BackgroundService(get(), get(), get(), get(), get()) }

	single { MarkdownRenderer(get()) }
	single { ItemLauncher() }
	single { KeyProcessor() }
	single { ReportingHelper(get(), get()) }
	single<PlaybackHelper> { SdkPlaybackHelper(get(), get(), get(), get()) }

	factory { (context: Context) -> SearchFragmentDelegate(context, get(), get()) }
}
