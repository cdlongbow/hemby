package org.emby.androidtv.ui.settings.screen.livetv

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import org.emby.androidtv.R
import org.emby.androidtv.preference.SystemPreferences
import org.emby.androidtv.ui.base.Text
import org.emby.androidtv.ui.base.form.Checkbox
import org.emby.androidtv.ui.base.list.ListButton
import org.emby.androidtv.ui.base.list.ListSection
import org.emby.androidtv.ui.settings.compat.rememberPreference
import org.emby.androidtv.ui.settings.composable.SettingsColumn
import org.koin.compose.koinInject

@Composable
fun SettingsLiveTvGuideFiltersScreen() {
	val systemPreferences = koinInject<SystemPreferences>()

	val filters = listOf(
		rememberPreference(systemPreferences, SystemPreferences.liveTvGuideFilterMovies) to stringResource(R.string.lbl_movies),
		rememberPreference(systemPreferences, SystemPreferences.liveTvGuideFilterSeries) to stringResource(R.string.lbl_series),
		rememberPreference(systemPreferences, SystemPreferences.liveTvGuideFilterNews) to stringResource(R.string.lbl_news),
		rememberPreference(systemPreferences, SystemPreferences.liveTvGuideFilterKids) to stringResource(R.string.lbl_kids),
		rememberPreference(systemPreferences, SystemPreferences.liveTvGuideFilterSports) to stringResource(R.string.lbl_sports),
		rememberPreference(systemPreferences, SystemPreferences.liveTvGuideFilterPremiere) to stringResource(R.string.lbl_new_only),
	)

	SettingsColumn {
		item {
			ListSection(
				overlineContent = { Text(stringResource(R.string.lbl_live_tv_guide).uppercase()) },
				headingContent = { Text(stringResource(R.string.filters)) },
			)
		}

		items(filters) { (preference, label) ->
			var enabled by preference

			ListButton(
				headingContent = { Text(label) },
				trailingContent = { Checkbox(checked = enabled) },
				onClick = { enabled = !enabled }
			)
		}
	}
}
