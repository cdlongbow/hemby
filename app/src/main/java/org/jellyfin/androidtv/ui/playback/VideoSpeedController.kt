package org.jellyfin.androidtv.ui.playback

import org.jellyfin.androidtv.preference.UserPreferences

class VideoSpeedController(
	private val parentController: PlaybackController,
	private val userPreferences: UserPreferences,
) {
	enum class SpeedSteps(val speed: Float) {
		SPEED_0_25(speed = 0.25f),
		SPEED_0_50(speed = 0.5f),
		SPEED_0_75(speed = 0.75f),
		SPEED_1_00(speed = 1.0f),
		SPEED_1_25(speed = 1.25f),
		SPEED_1_50(speed = 1.50f),
		SPEED_1_75(speed = 1.75f),
		SPEED_2_00(speed = 2.0f),
		SPEED_2_25(speed = 2.25f),
		SPEED_2_50(speed = 2.5f),
		SPEED_2_75(speed = 2.75f),
		SPEED_3_00(speed = 3.0f),
	}

	private fun resolveInitialSpeed(): SpeedSteps {
		val lastSpeed = userPreferences.get(UserPreferences.lastPlaybackSpeed)
		val speed = if (lastSpeed > 0f) lastSpeed else userPreferences.get(UserPreferences.playbackSpeed).speed
		return SpeedSteps.entries.firstOrNull { it.speed == speed } ?: SpeedSteps.SPEED_1_00
	}

	var currentSpeed = resolveInitialSpeed()
		set(value) {
			val checkedVal = if (parentController.isLiveTv) SpeedSteps.SPEED_1_00 else value
			parentController.setPlaybackSpeed(checkedVal.speed)
			userPreferences.set(UserPreferences.lastPlaybackSpeed, checkedVal.speed)
			field = checkedVal
		}

	init {
		currentSpeed = resolveInitialSpeed()
	}

	fun resetSpeedToDefault() {
		userPreferences.set(UserPreferences.lastPlaybackSpeed, -1.0f)
		currentSpeed = resolveInitialSpeed()
	}
}
