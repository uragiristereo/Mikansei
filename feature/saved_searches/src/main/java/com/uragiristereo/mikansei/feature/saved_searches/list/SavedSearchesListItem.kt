package com.uragiristereo.mikansei.feature.saved_searches.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun SavedSearchesListItem(
    item: SavedSearch,
    modifier: Modifier = Modifier,
    isEven: Boolean = false,
    isExpanded: Boolean = false,
    onToggleExpand: (Boolean) -> Unit = {},
    actions: SavedSearchesListItemActions = SavedSearchesListItemActions(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                when {
                    isEven -> MaterialTheme.colors.background.backgroundElevation(1.dp)
                    else -> MaterialTheme.colors.background
                }
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 8.dp,
                )
                .padding(vertical = 8.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 32.dp,
                        end = 8.dp,
                    ),
            ) {
                LabelsRow(
                    item = item,
                    onLabelClick = actions.onLabelClick,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.query,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            actions.onQueryClick(item.query)
                        }
                        .padding(all = 4.dp),
                )
            }

            Box {
                IconButton(
                    onClick = {
                        onToggleExpand(!isExpanded)
                    },
                    content = {
                        Icon(
                            painter = painterResource(
                                id = when {
                                    isExpanded -> R.drawable.expand_less
                                    else -> R.drawable.expand_more
                                },
                            ),
                            contentDescription = null,
                        )
                    },
                )
            }
        }

        ActionButtons(
            visible = isExpanded,
            item = item,
            actions = actions,
        )

        Divider()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LabelsRow(
    item: SavedSearch,
    onLabelClick: (label: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        item.labels.forEach { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.overline,
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        MaterialTheme.colors.background.backgroundElevation(4.dp)
                    )
                    .clickable {
                        onLabelClick(label)
                    }
                    .padding(all = 8.dp),
            )
        }
    }
}

@Composable
private fun ActionButtons(
    visible: Boolean,
    item: SavedSearch,
    actions: SavedSearchesListItemActions,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 48.dp),
        ) {
            TextButton(
                onClick = {
                    actions.onEditClick(item)
                },
                content = {
                    Text(text = "Edit".uppercase())
                },
            )

            TextButton(
                onClick = {
                    actions.onDeleteClick(item)
                },
                content = {
                    Text(text = "Delete".uppercase())
                },
            )
        }
    }
}

@Preview
@Composable
private fun SavedSearchesListItemPreview() {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    MikanseiTheme {
        Surface {
            Column {
                SavedSearchesListItem(
                    item = SavedSearch(
                        id = 0,
                        labels = listOf("miku", "migu"),
                        query = "hatsune_miku leek order:random",
                    ),
                )

                SavedSearchesListItem(
                    item = SavedSearch(
                        id = 1,
                        labels = listOf("rin", "rinchan"),
                        query = "kagamine_rin sailor_collar",
                    ),
                    isEven = true,
                    isExpanded = isExpanded,
                    onToggleExpand = { isExpanded = it },
                )
            }
        }
    }
}
