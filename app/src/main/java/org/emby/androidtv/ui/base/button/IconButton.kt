package org.emby.androidtv.ui.base.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.emby.androidtv.ui.base.EmbyTheme

object IconButtonDefaults {
	val Shape: Shape = ButtonDefaults.Shape
	val ContentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 10.dp)

	@ReadOnlyComposable
	@Composable
	fun colors(
		containerColor: Color = EmbyTheme.colorScheme.button,
		contentColor: Color = EmbyTheme.colorScheme.onButton,
		focusedContainerColor: Color = EmbyTheme.colorScheme.buttonFocused,
		focusedContentColor: Color = EmbyTheme.colorScheme.onButtonFocused,
		disabledContainerColor: Color = EmbyTheme.colorScheme.buttonDisabled,
		disabledContentColor: Color = EmbyTheme.colorScheme.onButtonDisabled,
	) = ButtonDefaults.colors(
		containerColor = containerColor,
		contentColor = contentColor,
		focusedContainerColor = focusedContainerColor,
		focusedContentColor = focusedContentColor,
		disabledContainerColor = disabledContainerColor,
		disabledContentColor = disabledContentColor,
	)
}

@Composable
fun IconButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	onLongClick: (() -> Unit)? = null,
	enabled: Boolean = true,
	shape: Shape = IconButtonDefaults.Shape,
	colors: ButtonColors = ButtonDefaults.colors(),
	contentPadding: PaddingValues = IconButtonDefaults.ContentPadding,
	interactionSource: MutableInteractionSource? = null,
	content: @Composable BoxScope.() -> Unit
) {
	ButtonBase(
		onClick = onClick,
		modifier = modifier,
		onLongClick = onLongClick,
		enabled = enabled,
		shape = shape,
		colors = colors,
		interactionSource = interactionSource,
	) {
		Box(
			modifier = Modifier
				.padding(contentPadding),
			content = content,
		)
	}
}
