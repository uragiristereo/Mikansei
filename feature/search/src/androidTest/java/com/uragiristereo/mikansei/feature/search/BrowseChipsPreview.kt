package com.uragiristereo.mikansei.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.search.BrowseChipType
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.feature.search.browse_chips.BrowseChip
import com.uragiristereo.mikansei.feature.search.browse_chips.BrowseChips
import com.uragiristereo.mikansei.feature.search.mock.expectedChips
import com.uragiristereo.mikansei.feature.search.mock.iseriChip
import com.uragiristereo.mikansei.feature.search.mock.oneBoyChip
import com.uragiristereo.mikansei.feature.search.mock.rupaChip
import com.uragiristereo.mikansei.feature.search.mock.tomoChip

@Preview
@Composable
fun BrowseChipPreview() {
    MikanseiTheme {
        Surface {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                BrowseChip(iseriChip)
                BrowseChip(oneBoyChip)
            }
        }
    }
}

@Preview
@Composable
fun BrowseChipOrPreview() {
    MikanseiTheme {
        Surface {
            BrowseChip(
                chips = BrowseChipType.Or(
                    tags = listOf(
                        tomoChip,
                        rupaChip,
                    ),
                ),
            )
        }
    }
}

@Preview
@Composable
fun BrowseChipsPreview() {
    MikanseiTheme {
        Surface {
            BrowseChips(
                chips = expectedChips,
                onClick = {},
            )
        }
    }
}
