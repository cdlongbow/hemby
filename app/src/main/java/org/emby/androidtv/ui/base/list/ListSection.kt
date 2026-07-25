package org.emby.androidtv.ui.base.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.emby.androidtv.ui.base.EmbyTheme

@Composable
fun ListSection(
	modifier: Modifier = Modifier,
	headingContent: @Composable () -> Unit,
	overlineContent: (@Composable () -> Unit)? = null,
	captionContent: (@Composable () -> Unit)? = null,
	leadingContent: (@Composable () -> Unit)? = null,
	trailingContent: (@Composable () -> Unit)? = null,
	footerContent: (@Composable () -> Unit)? = null,
) {
	ListItemContent(
		headingContent = headingContent,
		overlineContent = overlineContent,
		captionContent = captionContent,
		leadingContent = leadingContent,
		trailingContent = trailingContent,
		footerContent = footerContent,
		headingStyle = EmbyTheme.typography.listHeader
			.copy(color = EmbyTheme.colorScheme.listHeader),
		modifier = modifier,
	)
}
