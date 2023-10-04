package com.uragiristereo.mikansei.feature.image.more.tags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MoreTagsRow(
    tags: SnapshotStateList<Tag>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        tags.forEach { tag ->
            MoreTagItem(
                tag = tag,
                selected = false,
                onSelectedChange = { },
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }
    }
}
