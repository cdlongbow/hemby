package org.emby.androidtv.ui.settings.screen.playback.mediasegment

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.emby.androidtv.R
import org.emby.androidtv.ui.base.Text
import org.emby.androidtv.ui.base.list.ListButton
import org.emby.androidtv.ui.base.list.ListSection
import org.emby.androidtv.ui.playback.segment.MediaSegmentRepository
import org.emby.androidtv.ui.settings.composable.SettingsColumn
import org.koin.compose.koinInject

@Composable
fun SettingsPlaybackMediaSegmentsScreen() {
	val mediaSegmentRepository = koinInject<MediaSegmentRepository>()

	SettingsColumn {
		item {
			ListSection(
				overlineContent = { Text(stringResource(R.string.pref_playback).uppercase()) },
				headingContent = { Text(stringResource(R.string.pref_playback_media_segments)) },
			)
		}

		items(MediaSegmentRepository.SupportedTypes) { segmentType ->
			val action = mediaSegmentRepository.getDefaultSegmentTypeAction(segmentType)

			ListButton(
				headingContent = { Text(stringResource(segmentType.nameRes)) },
				captionContent = { Text(stringResource(action.nameRes)) },
				onClick = { }
			)
		}
	}
}
