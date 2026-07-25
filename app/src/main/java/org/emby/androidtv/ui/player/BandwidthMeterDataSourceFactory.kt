package org.emby.androidtv.ui.player

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@UnstableApi
class BandwidthMeterDataSourceFactory(
	private val delegate: DataSource.Factory,
	context: Context,
) : DataSource.Factory {
	private val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()
	private val _bandwidth = MutableStateFlow(0L)
	val bandwidth: StateFlow<Long> = _bandwidth.asStateFlow()

	override fun createDataSource(): DataSource {
		val ds = delegate.createDataSource()
		ds.addTransferListener(bandwidthMeter)
		return ds
	}

	fun updateBandwidth() {
		_bandwidth.value = bandwidthMeter.bitrateEstimate
	}
}