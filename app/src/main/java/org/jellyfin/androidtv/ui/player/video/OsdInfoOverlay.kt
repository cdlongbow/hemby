package org.jellyfin.androidtv.ui.player.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.player.BandwidthMeterDataSourceFactory
import org.jellyfin.playback.core.PlaybackManager
import org.jellyfin.playback.core.model.PositionInfo
import org.koin.compose.koinInject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration
import kotlin.time.DurationUnit

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

@Composable
fun OsdInfoOverlay(
    playbackManager: PlaybackManager = koinInject(),
    userPreferences: UserPreferences = koinInject(),
    bandwidthMeterFactory: BandwidthMeterDataSourceFactory = koinInject(),
) {
    val osdEnabled = userPreferences[UserPreferences.osdTimeCycleEnabled]
    if (!osdEnabled) return

    var currentTime by remember { mutableStateOf("") }
    var positionInfo by remember { mutableStateOf(PositionInfo.EMPTY) }
    val bandwidth by bandwidthMeterFactory.bandwidth.collectAsState()

    LaunchedEffect(Unit) {
        while (isActive) {
            currentTime = LocalDateTime.now().format(timeFormat)
            positionInfo = playbackManager.state.positionInfo
            bandwidthMeterFactory.updateBandwidth()
            delay(1000)
        }
    }

    val duration = positionInfo.duration
    if (duration == Duration.ZERO) return

    val includeHours = duration.inWholeMinutes >= 60
    val timeText = "${positionInfo.active.formatted(includeHours)} / ${duration.formatted(includeHours)}"

    val speedText = when {
        bandwidth <= 0 -> "0 KB/s"
        bandwidth < 1_000_000 -> "${bandwidth / 1000} KB/s"
        else -> "%.1f MB/s".format(bandwidth / 1_000_000f)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 80.dp)
        ) {
            Text(
                text = timeText,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 80.dp)
    }
}

private fun Duration.formatted(includeHours: Boolean): String {
    val totalSeconds = toInt(DurationUnit.SECONDS)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (includeHours) "%02d:%02d:%02d".format(hours, minutes, seconds)
    else "%02d:%02d".format(minutes, seconds)
}