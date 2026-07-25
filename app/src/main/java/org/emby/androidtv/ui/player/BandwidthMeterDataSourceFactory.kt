package org.emby.androidtv.ui.player

import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.DefaultBandwidthMeter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@UnstableApi
class BandwidthMeterDataSourceFactory(
	private val delegate: DataSource.Factory,
) : DataSource.Factory {
	private val bandwidthMeter = DefaultBandwidthMeter.Builder().build()
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