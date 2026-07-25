package org.emby.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.View
import org.emby.androidtv.R
import org.emby.androidtv.ui.playback.IntroOutroStore
import org.emby.androidtv.ui.playback.PlaybackController
import org.emby.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.emby.androidtv.ui.playback.overlay.VideoPlayerAdapter
import java.util.UUID

class IntroAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
	private val playbackController: PlaybackController,
	private val store: IntroOutroStore,
) : CustomAction(context, customPlaybackTransportControlGlue) {

	private var seriesId: UUID? = null

	init {
		initializeWithIcon(R.drawable.ic_tv_play)
		updateLabel()
	}

	fun setSeriesId(id: UUID?) {
		seriesId = id
		updateLabel()
	}

	fun getSeriesId() = seriesId

	fun getMarkerMs(): Long? {
		val sid = seriesId ?: return null
		return store.getMarkers(sid)?.introStartMs
	}

	private fun updateLabel() {
		val ms = getMarkerMs()
		val label = if (ms != null && ms > 0) formatTime(ms) else "--:--"
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
		if (pos > 0) {
			store.setIntroStartMs(sid, pos)
			updateLabel()
		}
	}

	fun adjustTime(deltaSeconds: Int) {
		val sid = seriesId ?: return
		val current = store.getMarkers(sid)?.introStartMs ?: return
		val newValue = (current + deltaSeconds * 1000L).coerceAtLeast(0)
		store.setIntroStartMs(sid, newValue)
		updateLabel()
	}

	companion object {
		fun formatTime(ms: Long): String {
			val totalSec = (ms / 1000).toInt()
			val min = totalSec / 60
			val sec = totalSec % 60
			return "%d:%02d".format(min, sec)
		}
	}
}