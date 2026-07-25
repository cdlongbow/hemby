package org.emby.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.View
import org.emby.androidtv.R
import org.emby.androidtv.ui.livetv.TvManager
import org.emby.androidtv.ui.playback.PlaybackController
import org.emby.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.emby.androidtv.ui.playback.overlay.LeanbackOverlayFragment
import org.emby.androidtv.ui.playback.overlay.VideoPlayerAdapter as VideoPlayerAdapter

class PreviousLiveTvChannelAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	init {
		initializeWithIcon(R.drawable.ic_previous_episode)
	}

	@Override
	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.masterOverlayFragment.switchChannel(TvManager.getPrevLiveTvChannel())
	}
}
