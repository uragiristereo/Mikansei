package com.uragiristereo.mikansei.feature.search.browse_chips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.getCategoryColor
import com.uragiristereo.mikansei.core.domain.module.search.BrowseChipType
import com.uragiristereo.mikansei.core.domain.module.search.TagType
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun BrowseChip(
    chip: BrowseChipType.Single.Regular,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
) {
    val isTagTypeExclude = chip.tagType == TagType.EXCLUDE
    val isLight = MaterialTheme.colors.isLight

    val tagColor = remember(chip.category, isLight) {
        chip.category?.getCategoryColor(isLight)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(size = 8.dp))
            .background(
                when {
                    isTagTypeExclude -> MaterialTheme.colors.error
                        .copy(alpha = 0.04f * elevation.value)
                        .compositeOver(MaterialTheme.colors.background)

                    else -> Color.Transparent
                }
            ),
    ) {
        if (isTagTypeExclude) {
            BrowseChipContainer(
                text = "—",
                contentColor = MaterialTheme.colors.onSurface,
                backgroundColor = MaterialTheme.colors.error
                    .copy(alpha = 0.04f * elevation.value)
                    .compositeOver(MaterialTheme.colors.background),
            )
        }

        BrowseChipContainer(
            text = chip.tag,
            contentColor = tagColor ?: MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.background
                .backgroundElevation(elevation),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrowseChip(
    chips: BrowseChipType.Or,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clip(RoundedCornerShape(size = 8.dp))
            .background(MaterialTheme.colors.background.backgroundElevation(2.dp))
            .padding(all = 4.dp),
    ) {
        chips.tags.forEachIndexed { index, chip ->
            when (chip) {
                is BrowseChipType.Single.Regular -> BrowseChip(chip, elevation = 4.dp)
                is BrowseChipType.Single.Qualifier -> BrowseChip(chip, elevation = 4.dp)
            }

            if (index != chips.tags.lastIndex) {
                Text(
                    text = "or",
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
        }
    }
}

@Composable
fun BrowseChip(
    chip: BrowseChipType.Single.Qualifier,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(size = 8.dp))
            .background(
                MaterialTheme.colors.onSurface
                    .copy(alpha = 0.04f * elevation.value)
                    .compositeOver(MaterialTheme.colors.background)
            ),
    ) {
        BrowseChipContainer(
            text = chip.first,
            contentColor = MaterialTheme.colors.primary,
            backgroundColor = MaterialTheme.colors.onSurface
                .copy(alpha = 0.04f * elevation.value)
                .compositeOver(MaterialTheme.colors.background),
        )

        BrowseChipContainer(
            text = chip.second,
            contentColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.background
                .backgroundElevation(elevation),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BrowseChips(
    chips: List<BrowseChipType>?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f),
        ) {
            if (chips == null) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(
                            horizontal = 6.dp,
                            vertical = 4.dp,
                        ),
                )
            }

            if (chips?.isEmpty() == true) {
                Text(
                    text = buildAnnotatedString {
                        append(text = stringResource(id = R.string.browse) + " ")

                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(text = stringResource(id = R.string.all_posts))
                        }
                    },
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(
                            horizontal = 6.dp,
                            vertical = 4.dp,
                        ),
                )
            }

            chips?.forEach { chip ->
                when (chip) {
                    is BrowseChipType.Single -> {
                        when (chip) {
                            is BrowseChipType.Single.Regular -> {
                                BrowseChip(
                                    chip = chip,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                )
                            }

                            is BrowseChipType.Single.Qualifier -> {
                                BrowseChip(
                                    chip = chip,
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                )
                            }
                        }

                    }

                    is BrowseChipType.Or -> {
                        BrowseChip(
                            chips = chip,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                }
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = stringResource(id = R.string.search_label),
        )
    }
}

@Composable
private fun BrowseChipContainer(
    text: String,
    contentColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle2,
        color = contentColor,
        modifier = modifier
            .clip(RoundedCornerShape(size = 8.dp))
            .background(backgroundColor)
            .padding(
                horizontal = 6.dp,
                vertical = 4.dp,
            ),
    )
}

@Preview
@Composable
private fun BrowseChipPreview() {
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
private fun BrowseChipOrPreview() {
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
private fun BrowseChipsPreview() {
    MikanseiTheme {
        Surface {
            BrowseChips(
                chips = expectedChips,
                onClick = {},
            )
        }
    }
}

val tomoChip = BrowseChipType.Single.Regular(
    tag = "ebizuka_tomo",
    category = Tag.Category.CHARACTER,
    tagType = TagType.INCLUDE,
)

val rupaChip = BrowseChipType.Single.Regular(
    tag = "rupa_(girls_band_cry)",
    category = Tag.Category.CHARACTER,
    tagType = TagType.INCLUDE,
)

val oneBoyChip = BrowseChipType.Single.Regular(
    tag = "1boy",
    category = null,
    tagType = TagType.EXCLUDE,
)

val iseriChip = BrowseChipType.Single.Regular(
    tag = "iseri",
    category = Tag.Category.ARTIST,
    tagType = TagType.INCLUDE,
)

val highresChip = BrowseChipType.Single.Regular(
    tag = "highres",
    category = Tag.Category.META,
    tagType = TagType.INCLUDE,
)

val microphoneChip = BrowseChipType.Single.Regular(
    tag = "microphone",
    category = Tag.Category.GENERAL,
    tagType = TagType.INCLUDE,
)

val ordfavChip = BrowseChipType.Single.Qualifier(
    first = "ordfav",
    second = "Mikansei_GitHub",
)

val expectedChips = listOf(
    highresChip,
    microphoneChip,
    oneBoyChip,
    BrowseChipType.Or(
        tags = listOf(
            tomoChip,
            rupaChip,
        ),
    ),
    iseriChip,
    ordfavChip,
)
