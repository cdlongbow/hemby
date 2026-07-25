package org.emby.androidtv.ui.settings.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import org.emby.androidtv.R
import org.emby.androidtv.preference.UserPreferences
import org.emby.androidtv.ui.base.Text
import org.emby.androidtv.ui.base.form.Checkbox
import org.emby.androidtv.ui.base.list.ListButton
import org.emby.androidtv.ui.base.list.ListSection
import org.emby.androidtv.ui.settings.compat.rememberPreference
import org.emby.androidtv.ui.settings.composable.SettingsColumn
import org.koin.compose.koinInject

@Composable
fun SettingsOsdScreen() {
	val userPreferences = koinInject<UserPreferences>()

	SettingsColumn {
		item {
			ListSection(
				overlineContent = { Text(stringResource(R.string.settings).uppercase()) },
				headingContent = { Text(stringResource(R.string.lbl_osd)) },
			)
		}

		item {
			var osdClockEnabled by rememberPreference(userPreferences, UserPreferences.osdClockEnabled)

			ListButton(
				headingContent = { Text(stringResource(R.string.lbl_osd_clock)) },
				trailingContent = { Checkbox(checked = osdClockEnabled) },
				captionContent = { Text(stringResource(R.string.lbl_osd_clock_description)) },
				onClick = { osdClockEnabled = !osdClockEnabled }
			)
		}

		item {
			var osdTimeEnabled by rememberPreference(userPreferences, UserPreferences.osdTimeEnabled)

			ListButton(
				headingContent = { Text(stringResource(R.string.lbl_osd_time)) },
				trailingContent = { Checkbox(checked = osdTimeEnabled) },
				captionContent = { Text(stringResource(R.string.lbl_osd_time_description)) },
				onClick = { osdTimeEnabled = !osdTimeEnabled }
			)
		}

		item {
			var osdNetworkSpeedEnabled by rememberPreference(userPreferences, UserPreferences.osdNetworkSpeedEnabled)

			ListButton(
				headingContent = { Text(stringResource(R.string.lbl_osd_network_speed)) },
				trailingContent = { Checkbox(checked = osdNetworkSpeedEnabled) },
				captionContent = { Text(stringResource(R.string.lbl_osd_network_speed_description)) },
				onClick = { osdNetworkSpeedEnabled = !osdNetworkSpeedEnabled }
			)
		}
	}
}