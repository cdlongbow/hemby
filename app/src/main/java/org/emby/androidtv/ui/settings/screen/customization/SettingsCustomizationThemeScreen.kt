package org.emby.androidtv.ui.settings.screen.customization

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import org.emby.androidtv.R
import org.emby.androidtv.preference.UserPreferences
import org.emby.androidtv.preference.constant.AppTheme
import org.emby.androidtv.ui.base.Text
import org.emby.androidtv.ui.base.form.RadioButton
import org.emby.androidtv.ui.base.list.ListButton
import org.emby.androidtv.ui.base.list.ListSection
import org.emby.androidtv.ui.navigation.LocalRouter
import org.emby.androidtv.ui.settings.compat.rememberPreference
import org.emby.androidtv.ui.settings.composable.SettingsColumn
import org.koin.compose.koinInject

@Composable
fun SettingsCustomizationThemeScreen() {
	val router = LocalRouter.current
	val userPreferences = koinInject<UserPreferences>()
	var appTheme by rememberPreference(userPreferences, UserPreferences.appTheme)

	SettingsColumn {
		item {
			ListSection(
				overlineContent = { Text(stringResource(R.string.pref_customization).uppercase()) },
				headingContent = { Text(stringResource(R.string.pref_app_theme)) },
			)
		}

		items(AppTheme.entries) { entry ->
			ListButton(
				headingContent = { Text(stringResource(entry.nameRes)) },
				trailingContent = { RadioButton(checked = appTheme == entry) },
				onClick = {
					appTheme = entry
					router.back()
				}
			)
		}
	}
}
