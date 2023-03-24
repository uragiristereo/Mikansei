package com.uragiristereo.mikansei.core

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.component.ProductAlertDialog
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun ShareDownloadDialog(
    downloadState: DownloadState,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProductAlertDialog(
        onDismissRequest = { },
        title = {
            Text(text = "Downloading...")
        },
        text = {
            Column {
                val animatedProgress by animateFloatAsState(targetValue = downloadState.progress)

                when (downloadState.progress) {
                    0f -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

                    else -> LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                ) {
                    val progressFormatted = "%.2f".format(downloadState.progress.times(100))

                    Text(text = "$progressFormatted%")

                    Text(text = downloadState.downloadSpeed)
                }

                Text(
                    text = "${downloadState.downloaded} / ${downloadState.fileSize}",
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        buttons = {
            TextButton(
                onClick = onCancelClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colors.primary
                        .copy(alpha = 0.74f),
                ),
                content = {
                    Text(text = stringResource(id = R.string.cancel_action).uppercase())
                },
            )
        },
        modifier = modifier,
    )
}
