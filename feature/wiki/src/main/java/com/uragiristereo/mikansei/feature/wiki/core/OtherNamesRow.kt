package com.uragiristereo.mikansei.feature.wiki.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.ui.composable.SidesGradient2
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun OtherNamesRow(
    names: List<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SidesGradient2(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(names) { name ->
                Text(
                    text = name,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .height(IntrinsicSize.Max)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colors.background.backgroundElevation())
                        .clickable {
                            onItemClick(name)
                        }
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp,
                        ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun OtherNamesRowPreview() {
    MikanseiTheme {
        Surface {
            OtherNamesRow(
                names = listOf(
                    "プラチナ(アークナイツ)",
                    "白金",
                    "白金(明日方舟)",
                    "bang_dream",
                ),
                onItemClick = {},
            )
        }
    }
}
