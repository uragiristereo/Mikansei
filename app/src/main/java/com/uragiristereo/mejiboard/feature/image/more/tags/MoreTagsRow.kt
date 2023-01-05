package com.uragiristereo.mejiboard.feature.image.more.tags

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.uragiristereo.mejiboard.domain.entity.booru.tag.Tag

@Composable
fun MoreTagsRow(
    tags: SnapshotStateList<Tag>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        crossAxisAlignment = FlowCrossAxisAlignment.Center,
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 12.dp,
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        tags.forEach { tag ->
            MoreTagItem(
                tag = tag,
                selected = false,
                onSelectedChange = { },
            )
        }
    }
}
