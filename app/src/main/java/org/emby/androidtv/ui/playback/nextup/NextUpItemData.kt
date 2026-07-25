package org.emby.androidtv.ui.playback.nextup

import org.emby.androidtv.util.apiclient.EmbyImage
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.BaseItemDto

data class NextUpItemData(
	val baseItem: BaseItemDto,
	val id: UUID,
	val title: String,
	val thumbnail: EmbyImage?,
	val logo: EmbyImage?,
)
