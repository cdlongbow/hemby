package org.emby.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.View
import org.emby.androidtv.R
import org.emby.androidtv.ui.playback.PlaybackController
import org.emby.androidtv.ui.playback.overlay.VideoPlayerAdapter
import org.emby.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue

class EpisodeAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	init {
		initializeWithIcon(R.drawable.ic_select_chapter)
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		// Open episode list popup
		videoPlayerAdapter.leanbackOverlayFragment.hideOverlay()
		videoPlayerAdapter.masterOverlayFragment.showEpisodeSelector()
	}
}