package org.emby.androidtv.preference.constant

import org.emby.androidtv.R
import org.jellyfin.preference.PreferenceEnum

enum class PlaybackSpeed(
    override val nameRes: Int,
    val speed: Float,
) : PreferenceEnum {
    SPEED_0_25(R.string.playback_speed_0_25, 0.25f),
    SPEED_0_50(R.string.playback_speed_0_50, 0.5f),
    SPEED_0_75(R.string.playback_speed_0_75, 0.75f),
    SPEED_1_00(R.string.playback_speed_1_00, 1.0f),
    SPEED_1_25(R.string.playback_speed_1_25, 1.25f),
    SPEED_1_50(R.string.playback_speed_1_50, 1.50f),
    SPEED_1_75(R.string.playback_speed_1_75, 1.75f),
    SPEED_2_00(R.string.playback_speed_2_00, 2.0f),
    SPEED_2_25(R.string.playback_speed_2_25, 2.25f),
    SPEED_2_50(R.string.playback_speed_2_50, 2.5f),
    SPEED_2_75(R.string.playback_speed_2_75, 2.75f),
    SPEED_3_00(R.string.playback_speed_3_00, 3.0f),
}