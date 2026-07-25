package org.emby.androidtv.ui.playback

import org.emby.androidtv.preference.UserPreferences
import org.jellyfin.sdk.model.api.BaseItemDto
import org.json.JSONObject
import java.util.UUID

data class SeriesMarkers(
	val introStartMs: Long?,
	val outroEndMs: Long?,
)

class IntroOutroStore(private val userPreferences: UserPreferences) {

	fun getMarkers(item: BaseItemDto): SeriesMarkers? {
		val seriesId = item.seriesId ?: return null
		return getMarkers(seriesId)
	}

	fun getMarkers(seriesId: UUID): SeriesMarkers? {
		val json = JSONObject(userPreferences.get(UserPreferences.manualIntroOutro))
		val entry = json.optJSONObject(seriesId.toString()) ?: return null
		return SeriesMarkers(
			introStartMs = if (entry.has("introStartMs")) entry.getLong("introStartMs") else null,
			outroEndMs = if (entry.has("outroEndMs")) entry.getLong("outroEndMs") else null,
		)
	}

	fun setIntroStartMs(seriesId: UUID, introStartMs: Long) {
		val json = JSONObject(userPreferences.get(UserPreferences.manualIntroOutro))
		val entry = json.optJSONObject(seriesId.toString()) ?: JSONObject()
		entry.put("introStartMs", introStartMs)
		json.put(seriesId.toString(), entry)
		userPreferences.set(UserPreferences.manualIntroOutro, json.toString())
	}

	fun setOutroEndMs(seriesId: UUID, outroEndMs: Long) {
		val json = JSONObject(userPreferences.get(UserPreferences.manualIntroOutro))
		val entry = json.optJSONObject(seriesId.toString()) ?: JSONObject()
		entry.put("outroEndMs", outroEndMs)
		json.put(seriesId.toString(), entry)
		userPreferences.set(UserPreferences.manualIntroOutro, json.toString())
	}

	fun clearSeries(seriesId: UUID) {
		val json = JSONObject(userPreferences.get(UserPreferences.manualIntroOutro))
		json.remove(seriesId.toString())
		userPreferences.set(UserPreferences.manualIntroOutro, json.toString())
	}
}