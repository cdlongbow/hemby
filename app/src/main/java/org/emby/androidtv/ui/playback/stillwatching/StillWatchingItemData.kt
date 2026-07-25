package org.emby.androidtv.ui.playback.stillwatching

import org.emby.androidtv.util.apiclient.EmbyImage
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.BaseItemDto

data class StillWatchingItemData(
	val baseItem: BaseItemDto,
	val id: UUID,
	val title: String,
	val thumbnail: EmbyImage?,
	val logo: EmbyImage?,
)
