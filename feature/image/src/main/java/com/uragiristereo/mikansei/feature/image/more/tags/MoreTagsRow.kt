package com.uragiristereo.mikansei.feature.image.more.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.resources.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MoreTagsRow(
    tags: SnapshotStateList<Tag>,
    tagsCount: Int,
    onTagClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.image_n_tags, tagsCount),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            tags.forEach { tag ->
                MoreTagItem(
                    tag = tag,
                    selected = false,
                    onSelectedChange = {
                        onTagClick(tag.name)
                    },
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
        }
    }
}
