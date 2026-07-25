package org.emby.androidtv.preference.constant

import org.emby.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class BackdropBehavior(
	override val nameRes: Int,
) : PreferenceEnum {
	DISABLED(R.string.state_disabled),

	/**
	 * When selected, backdrop is shown with a blur effect.
	 */
	BACKDROP_WITH_BLUR(R.string.backdrop_behavior_with_blur),

	/**
	 * When selected, backdrop is shown without any blur effect.
	 */
	BACKDROP_WITHOUT_BLUR(R.string.backdrop_behavior_without_blur),
}
