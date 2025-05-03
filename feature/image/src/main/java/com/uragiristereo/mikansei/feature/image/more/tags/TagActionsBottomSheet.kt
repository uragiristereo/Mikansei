package com.uragiristereo.mikansei.feature.image.more.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.extension.copyToClipboard
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.bottomSheetContentPadding
import kotlinx.coroutines.launch

@Composable
fun TagActionsBottomSheet(
    tag: String,
    currentSearchTags: String,
    onDismiss: suspend () -> Unit,
    onNavigateBack: () -> Unit,
    onAddToExisting: (searchImmediately: Boolean) -> Unit,
    onExcludeToExisting: (searchImmediately: Boolean) -> Unit,
    onNewSearch: (searchImmediately: Boolean) -> Unit,
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .bottomSheetContentPadding(),
    ) {
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.background.copy(alpha = ContentAlpha.medium)),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = null,
                )
            },
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 8.dp,
                    bottom = 8.dp,
                ),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tag Actions",
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.87f),
                )

                Text(
                    text = tag,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    context.copyToClipboard(
                        text = tag,
                        message = "Tag copied to clipboard!",
                    )
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.content_copy),
                    contentDescription = null,
                )
            }
        }

        Divider()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
        ) {
            ClickableSection(
                title = "Add to existing search",
                subtitle = buildAnnotatedString {
                    append(currentSearchTags)

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Medium,
                        ),
                    ) {
                        append(tag)
                    }
                },
                icon = painterResource(R.drawable.variable_insert),
                onClick = {
                    scope.launch {
                        onDismiss()
                        onAddToExisting(true)
                    }
                },
                onLongClick = {
                    scope.launch {
                        onDismiss()
                        onAddToExisting(false)
                    }
                },
            )

            ClickableSection(
                title = "Exclude from existing search",
                subtitle = buildAnnotatedString {
                    append(currentSearchTags)

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.error,
                            fontWeight = FontWeight.Medium,
                        ),
                    ) {
                        append("-$tag")
                    }
                },
                icon = painterResource(R.drawable.variable_remove),
                onClick = {
                    scope.launch {
                        onDismiss()
                        onExcludeToExisting(true)
                    }
                },
                onLongClick = {
                    scope.launch {
                        onDismiss()
                        onExcludeToExisting(false)
                    }
                },
            )

            ClickableSection(
                title = "New search from this tag",
                icon = painterResource(R.drawable.variable_add),
                onClick = {
                    scope.launch {
                        onDismiss()
                        onNewSearch(true)
                    }
                },
                onLongClick = {
                    scope.launch {
                        onDismiss()
                        onNewSearch(false)
                    }
                },
            )
        }
    }
}
