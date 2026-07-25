package org.emby.androidtv.ui.itemhandling

import android.content.Context
import org.emby.androidtv.constant.ImageType
import org.emby.androidtv.ui.GridButton

class GridButtonBaseRowItem(
	val gridButton: GridButton,
) : BaseRowItem(
	baseRowType = BaseRowType.GridButton,
	staticHeight = true,
) {
	override fun getImage(imageType: ImageType) = null
	override fun getFullName(context: Context) = gridButton.text
	override fun getName(context: Context) = gridButton.text
}
