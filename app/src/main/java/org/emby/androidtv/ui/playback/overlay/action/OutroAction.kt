package org.emby.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.View
import org.emby.androidtv.R
import org.emby.androidtv.ui.playback.IntroOutroStore
import org.emby.androidtv.ui.playback.PlaybackController
import org.emby.androidtv.ui.playback.overlay.VideoPlayerAdapter
import org.emby.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import java.util.UUID

class OutroAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
	private val playbackController: PlaybackController,
	private val store: IntroOutroStore,
) : CustomAction(context, customPlaybackTransportControlGlue) {

	private var seriesId: UUID? = null
	private var duration: Long = 0

	init {
		initializeWithIcon(R.drawable.ic_next)
		updateLabel()
	}

	fun setSeriesId(id: UUID?) {
		seriesId = id
		updateLabel()
	}

	fun setDuration(dur: Long) {
		duration = dur
	}

	fun getSeriesId() = seriesId

	fun getMarkerMs(): Long? {
		val sid = seriesId ?: return null
		return store.getMarkers(sid)?.outroEndMs
	}

	private fun updateLabel() {
		val ms = getMarkerMs()
		val label = if (ms != null && ms > 0) "-${IntroAction.formatTime(ms)}" else "--:--"
		setLabels(arrayOf(label))
		notifyActionChanged()
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		val sid = seriesId ?: return
		val pos = playbackController.currentPosition
		val dur = if (duration > 0) duration else playbackController.duration
		if (dur > 0 && pos > 0) {
			val fromEnd = dur - pos
			store.setOutroEndMs(sid, fromEnd)
			updateLabel()
		}
	}

	fun adjustTime(deltaSeconds: Int) {
		val sid = seriesId ?: return
		val current = store.getMarkers(sid)?.outroEndMs ?: return
		val newValue = (current + deltaSeconds * 1000L).coerceAtLeast(0)
		store.setOutroEndMs(sid, newValue)
		updateLabel()
	}
}