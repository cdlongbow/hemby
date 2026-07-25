package org.emby.androidtv.ui.playback.overlay.action

import org.emby.androidtv.ui.playback.overlay.VideoPlayerAdapter

interface AndroidAction {
	fun onActionClicked(
		videoPlayerAdapter: VideoPlayerAdapter
	)
}
