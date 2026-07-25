package org.emby.androidtv.ui.settings.screen.playback

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import org.emby.androidtv.R
import org.emby.androidtv.preference.UserPreferences
import org.emby.androidtv.ui.base.Text
import org.emby.androidtv.ui.base.form.RadioButton
import org.emby.androidtv.ui.base.list.ListButton
import org.emby.androidtv.ui.base.list.ListSection
import org.emby.androidtv.ui.navigation.LocalRouter
import org.emby.androidtv.ui.settings.compat.rememberPreference
import org.emby.androidtv.ui.settings.composable.SettingsColumn
import org.koin.compose.koinInject

@Composable
fun SettingsPlaybackResumeSubtractDurationScreen() {
	val router = LocalRouter.current
	val userPreferences = koinInject<UserPreferences>()
	var resumeSubtractDuration by rememberPreference(userPreferences, UserPreferences.resumeSubtractDuration)
	val options = getResumeSubtractDurationOptions()

	SettingsColumn {
		item {
			ListSection(
				overlineContent = { Text(stringResource(R.string.pref_playback_advanced).uppercase()) },
				headingContent = { Text(stringResource(R.string.lbl_resume_preroll)) },
			)
		}

		items(options.toList()) { (value, label) ->
			ListButton(
				headingContent = { Text(label) },
				trailingContent = { RadioButton(checked = resumeSubtractDuration == value) },
				onClick = {
					resumeSubtractDuration = value
					router.back()
				}
			)
		}
	}
}
