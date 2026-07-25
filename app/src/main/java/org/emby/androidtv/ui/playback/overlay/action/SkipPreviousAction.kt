package org.emby.androidtv.ui.playback.overlay.action

import android.content.Context
import androidx.leanback.widget.PlaybackControlsRow
import org.emby.androidtv.ui.playback.overlay.VideoPlayerAdapter

class SkipPreviousAction(context: Context) : PlaybackControlsRow.SkipPreviousAction(context),
	AndroidAction {
	override fun onActionClicked(videoPlayerAdapter: VideoPlayerAdapter) =
		videoPlayerAdapter.previous()
}
