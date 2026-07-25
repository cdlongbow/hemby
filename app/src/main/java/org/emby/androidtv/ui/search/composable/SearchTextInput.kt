package org.emby.androidtv.ui.search.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.emby.androidtv.R
import org.emby.androidtv.ui.base.Icon
import org.emby.androidtv.ui.base.EmbyTheme
import org.emby.androidtv.ui.base.LocalTextStyle
import org.emby.androidtv.ui.base.ProvideTextStyle

@Composable
fun SearchTextInput(
	query: String,
	onQueryChange: (query: String) -> Unit,
	onQuerySubmit: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val interactionSource = remember { MutableInteractionSource() }
	val focused by interactionSource.collectIsFocusedAsState()

	val color = when {
		focused -> EmbyTheme.colorScheme.inputFocused to EmbyTheme.colorScheme.onInputFocused
		else -> EmbyTheme.colorScheme.input to EmbyTheme.colorScheme.onInput
	}

	ProvideTextStyle(
		LocalTextStyle.current.copy(
			color = color.second,
			fontSize = 16.sp,
		)
	) {
		BasicTextField(
			modifier = modifier,
			value = query,
			singleLine = true,
			interactionSource = interactionSource,
			onValueChange = { onQueryChange(it) },
			keyboardActions = KeyboardActions { onQuerySubmit() },
			keyboardOptions = KeyboardOptions.Default.copy(
				keyboardType = KeyboardType.Text,
				imeAction = ImeAction.Search,
				autoCorrectEnabled = true,
				// Note: Compose does not support a press to open functionality (yet?) or programmatic keyboard activation so we can only
				// use the show on focus behavior. Unfortunately this does not work great with some vendors like Amazon.
				// In addition, this boolean cannot be unset with the (stateless) BasicTextField implementation we're using
				showKeyboardOnFocus = true,
			),
			textStyle = LocalTextStyle.current,
			cursorBrush = SolidColor(color.first),
			decorationBox = { innerTextField ->
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.border(2.dp, color.first, RoundedCornerShape(percent = 30))
						.padding(12.dp)
				) {
					Icon(ImageVector.vectorResource(R.drawable.ic_search), contentDescription = null)
					Spacer(Modifier.width(12.dp))
					innerTextField()
				}
			}
		)
	}
}
