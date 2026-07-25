package org.emby.androidtv.data.model

import org.emby.androidtv.util.apiclient.EmbyImage
import java.util.UUID

data class ChapterItemInfo(
	val itemId: UUID,
	val name: String?,
	val startPositionTicks: Long,
	val image: EmbyImage?,
)
