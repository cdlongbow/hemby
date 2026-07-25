package org.jellyfin.androidtv.ui.playback

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.constant.PlaybackSpeed

class VideoSpeedControllerTests : FunSpec({
    fun createMockUserPrefs(
        defaultSpeed: PlaybackSpeed = PlaybackSpeed.SPEED_1_00,
        lastSpeed: Float = -1.0f,
    ): UserPreferences {
        val prefs = mockk<UserPreferences>(relaxed = true)
        every { prefs.get(UserPreferences.playbackSpeed) } returns defaultSpeed
        every { prefs.get(UserPreferences.lastPlaybackSpeed) } returns lastSpeed
        justRun { prefs.set(UserPreferences.lastPlaybackSpeed, any<Float>()) }
        return prefs
    }

    afterTest {
        val mockPrefs = createMockUserPrefs()
        VideoSpeedController(mockk(relaxed = true), mockPrefs).resetSpeedToDefault()
    }

    test("VideoSpeedController.SpeedSteps uses intervals of 0.25") {
        VideoSpeedController.SpeedSteps.entries.forEachIndexed { i, v ->
            v.speed.shouldBe((i + 1) * 0.25f plusOrMinus 1.0E-4F)
        }
    }

    test("VideoSpeedController uses global default when lastPlaybackSpeed is not set") {
        val mockController = mockk<PlaybackController>(relaxed = true)
        val slot = slot<Float>()
        justRun { mockController.setPlaybackSpeed(capture(slot)) }

        VideoSpeedController(mockController, createMockUserPrefs())

        verify { mockController.setPlaybackSpeed(any()) }
        slot.captured shouldBe (1.0f plusOrMinus 1.0E-4F)
    }

    test("VideoSpeedController uses lastPlaybackSpeed when set") {
        val mockController = mockk<PlaybackController>(relaxed = true)
        val slot = slot<Float>()
        justRun { mockController.setPlaybackSpeed(capture(slot)) }

        VideoSpeedController(mockController, createMockUserPrefs(lastSpeed = 2.0f))

        verify { mockController.setPlaybackSpeed(any()) }
        slot.captured shouldBe (2.0f plusOrMinus 1.0E-4F)
    }

    test("VideoSpeedController.currentSpeed getter returns set value") {
        val mockController = mockk<PlaybackController>(relaxed = true)
        every { mockController.isLiveTv } returns false
        val controller = VideoSpeedController(mockController, createMockUserPrefs())

        val expected = VideoSpeedController.SpeedSteps.SPEED_1_25
        controller.currentSpeed = expected

        controller.currentSpeed shouldBe expected
    }

    test("VideoSpeedController.currentSpeed updates the speed in the controller") {
        val mockController = mockk<PlaybackController>(relaxed = true)
        every { mockController.isLiveTv } returns false
        val slot = slot<Float>()
        justRun { mockController.setPlaybackSpeed(capture(slot)) }

        val controller = VideoSpeedController(mockController, createMockUserPrefs())
        val expected = VideoSpeedController.SpeedSteps.SPEED_1_75
        controller.currentSpeed = expected

        verify { mockController.setPlaybackSpeed(any()) }
        slot.captured shouldBe (expected.speed plusOrMinus 1.0E-4F)
    }

    test("VideoSpeedController persists speed to lastPlaybackSpeed on popup change") {
        val mockController = mockk<PlaybackController>(relaxed = true)
        every { mockController.isLiveTv } returns false
        val mockPrefs = createMockUserPrefs()
        val controller = VideoSpeedController(mockController, mockPrefs)

        controller.currentSpeed = VideoSpeedController.SpeedSteps.SPEED_2_00

        verify { mockPrefs.set(UserPreferences.lastPlaybackSpeed, 2.0f) }
    }

    test("VideoSpeedController.resetSpeedToDefault() clears lastPlaybackSpeed and uses global default") {
        val playbackController = mockk<PlaybackController>(relaxed = true)
        every { playbackController.isLiveTv } returns false
        val mockPrefs = createMockUserPrefs()
        val videoController = VideoSpeedController(playbackController, mockPrefs)

        videoController.currentSpeed = VideoSpeedController.SpeedSteps.SPEED_2_00
        videoController.resetSpeedToDefault()

        verify { mockPrefs.set(UserPreferences.lastPlaybackSpeed, -1.0f) }

        val slot = slot<Float>()
        justRun { playbackController.setPlaybackSpeed(capture(slot)) }
        VideoSpeedController(playbackController, mockPrefs)

        verify { playbackController.setPlaybackSpeed(any()) }
        slot.captured shouldBe (1.0f plusOrMinus 1.0E-4F)
    }

    test("VideoSpeedController.currentSpeed always sets the speed to 1 for LiveTV") {
        val mockPrefs = createMockUserPrefs(PlaybackSpeed.SPEED_2_00)

        VideoSpeedController(mockk(relaxed = true), mockPrefs).currentSpeed = VideoSpeedController.SpeedSteps.SPEED_2_00

        val mockController = mockk<PlaybackController>(relaxed = true) {
            every { isLiveTv } returns true
        }
        val speedController = VideoSpeedController(mockController, mockPrefs)

        verify { mockController.setPlaybackSpeed(1.0f) }
        speedController.currentSpeed shouldBe VideoSpeedController.SpeedSteps.SPEED_1_00

        speedController.currentSpeed = VideoSpeedController.SpeedSteps.SPEED_2_00

        verify { mockController.setPlaybackSpeed(1.0f) }
        speedController.currentSpeed shouldBe VideoSpeedController.SpeedSteps.SPEED_1_00
    }

    test("VideoSpeedController.currentSpeed always sets the requested speed when LiveTV is off") {
        val mockController = mockk<PlaybackController>(relaxed = true) {
            every { isLiveTv } returns false
        }
        val speedController = VideoSpeedController(mockController, createMockUserPrefs())

        verify { mockController.setPlaybackSpeed(1.0f) }
        speedController.currentSpeed shouldBe VideoSpeedController.SpeedSteps.SPEED_1_00

        speedController.currentSpeed = VideoSpeedController.SpeedSteps.SPEED_2_00

        verify { mockController.setPlaybackSpeed(2.0f) }
        speedController.currentSpeed shouldBe VideoSpeedController.SpeedSteps.SPEED_2_00
    }
})