package org.emby.androidtv.util.apiclient

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class EmbyUrlInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()
		val originalUrl = originalRequest.url
		val path = originalUrl.encodedPath

		if (path.startsWith("/emby")) {
			Timber.v("EmbyUrlInterceptor: path already has /emby prefix, skipping: %s", path)
			return chain.proceed(originalRequest)
		}

		val newUrl = originalUrl.newBuilder()
			.encodedPath("/emby$path")
			.build()

		Timber.d("EmbyUrlInterceptor: %s -> %s", path, newUrl.encodedPath)

		val newRequest = originalRequest.newBuilder()
			.url(newUrl)
			.build()

		return chain.proceed(newRequest)
	}
}