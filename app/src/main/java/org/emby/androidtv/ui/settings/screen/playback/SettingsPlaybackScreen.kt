package org.emby.androidtv.ui.settings.screen.playback

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.emby.androidtv.R
import org.emby.androidtv.data.repository.ExternalAppRepository
import org.emby.androidtv.preference.UserPreferences
import org.emby.androidtv.ui.base.Icon
import org.emby.androidtv.ui.base.LocalShapes
import org.emby.androidtv.ui.base.Text
import org.emby.androidtv.ui.base.list.ListButton
import org.emby.androidtv.ui.base.list.ListSection
import org.emby.androidtv.ui.navigation.LocalRouter
import org.emby.androidtv.ui.settings.Routes
import org.emby.androidtv.ui.settings.compat.rememberPreference
import org.emby.androidtv.ui.settings.composable.SettingsColumn
import org.koin.compose.koinInject

@Composable
fun SettingsPlaybackScreen() {
	val context = LocalContext.current
	val router = LocalRouter.current
	val externalAppRepository = koinInject<ExternalAppRepository>()
	val userPreferences = koinInject<UserPreferences>()

	SettingsColumn {
		item {
			ListSection(
				overlineContent = { Text(stringResource(R.string.settings).uppercase()) },
				headingContent = { Text(stringResource(R.string.pref_playback)) },
			)
		}

		item {
			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_tv_play), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.playback_video_player)) },
				trailingContent = {
					val iconDrawable = remember(context) {
						externalAppRepository.getCurrentExternalPlayerApp(context)?.loadIcon(context.packageManager)
					}
					Image(
						painter = if (iconDrawable == null) rememberAsyncImagePainter(R.mipmap.app_icon)
						else rememberAsyncImagePainter(iconDrawable),
						contentDescription = null,
						modifier = Modifier
							.size(24.dp)
							.clip(LocalShapes.current.small)
					)
				},
				onClick = { router.push(Routes.PLAYBACK_PLAYER) }
			)
		}

		item {
			var playbackSpeed by rememberPreference(userPreferences, UserPreferences.playbackSpeed)

			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_playback_speed), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.lbl_playback_speed)) },
				captionContent = { Text(stringResource(playbackSpeed.nameRes)) },
				onClick = { router.push(Routes.PLAYBACK_SPEED) }
			)
		}

		item {
			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_next_up), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.pref_playback_next_up)) },
				onClick = { router.push(Routes.PLAYBACK_NEXT_UP) }
			)
		}

		item {
			var stillWatchingBehavior by rememberPreference(userPreferences, UserPreferences.stillWatchingBehavior)
			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_zzz), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.pref_playback_inactivity_prompt)) },
				captionContent = { Text(stringResource(stillWatchingBehavior.nameRes)) },
				onClick = { router.push(Routes.PLAYBACK_INACTIVITY_PROMPT) }
			)
		}

		item {
			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_trailer), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.pref_playback_prerolls)) },
				onClick = { router.push(Routes.PLAYBACK_PREROLLS) }
			)
		}

		item {
			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_subtitles), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.pref_customization_subtitles)) },
				onClick = { router.push(Routes.CUSTOMIZATION_SUBTITLES) }
			)
		}

		item {
			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_clapperboard), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.pref_playback_media_segments)) },
				onClick = { router.push(Routes.PLAYBACK_MEDIA_SEGMENTS) }
			)
		}

		item {
			ListButton(
				leadingContent = { Icon(painterResource(R.drawable.ic_more), contentDescription = null) },
				headingContent = { Text(stringResource(R.string.pref_playback_advanced)) },
				onClick = { router.push(Routes.PLAYBACK_ADVANCED) }
			)
		}
	}
}
