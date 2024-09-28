package com.uragiristereo.mikansei.feature.search

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.search.BrowseChipType
import com.uragiristereo.mikansei.core.domain.usecase.GenerateChipsFromTagsUseCase
import com.uragiristereo.mikansei.feature.search.mock.expectedChips
import com.uragiristereo.mikansei.feature.search.mock.highresChip
import com.uragiristereo.mikansei.feature.search.mock.iseriChip
import com.uragiristereo.mikansei.feature.search.mock.microphoneChip
import com.uragiristereo.mikansei.feature.search.mock.oneBoyChip
import com.uragiristereo.mikansei.feature.search.mock.ordfavChip
import com.uragiristereo.mikansei.feature.search.mock.rupaChip
import com.uragiristereo.mikansei.feature.search.mock.tomoChip
import kotlin.test.Test

class BrowseChipsTest {
    val generateChipsFromTagsUseCase = GenerateChipsFromTagsUseCase()

    companion object {
        private val cachedTags: Map<String, Tag.Category> = mapOf(
            "highres" to Tag.Category.META,
            "microphone" to Tag.Category.GENERAL,
            "ebizuka_tomo" to Tag.Category.CHARACTER,
            "rupa_(girls_band_cry)" to Tag.Category.CHARACTER,
            "iseri" to Tag.Category.ARTIST,
        )
    }

    @Test
    fun `generate chips 1`() {
        val tags =
            "highres microphone -1boy (ebizuka_tomo or rupa_(girls_band_cry)) iseri ordfav:Mikansei_GitHub"
        val expected = expectedChips
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }

    @Test
    fun `generate chips 2`() {
        val tags = "highres microphone -1boy ebizuka_tomo rupa_(girls_band_cry) iseri"
        val expected = listOf(
            highresChip,
            microphoneChip,
            oneBoyChip,
            tomoChip,
            rupaChip,
            iseriChip,
        )
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }

    @Test
    fun `generate chips 3`() {
        val tags = "(ebizuka_tomo or rupa_(girls_band_cry)) highres microphone -1boy iseri"
        val expected = listOf(
            BrowseChipType.Or(
                listOf(
                    tomoChip,
                    rupaChip,
                ),
            ),
            highresChip,
            microphoneChip,
            oneBoyChip,
            iseriChip,
        )
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }

    @Test
    fun `generate chips 4`() {
        val tags = "highres microphone -1boy iseri (ebizuka_tomo or rupa_(girls_band_cry))"
        val expected = listOf(
            highresChip,
            microphoneChip,
            oneBoyChip,
            iseriChip,
            BrowseChipType.Or(
                listOf(
                    tomoChip,
                    rupaChip,
                ),
            ),
        )
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }

    @Test
    fun `generate chips 5`() {
        val tags = "highres microphone -1boy (ebizuka_tomo or rupa_(girls_band_cry) or iseri)"
        val expected = listOf(
            highresChip,
            microphoneChip,
            oneBoyChip,
            BrowseChipType.Or(
                listOf(
                    tomoChip,
                    rupaChip,
                    iseriChip,
                ),
            ),
        )
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }

    @Test
    fun `generate chips 6`() {
        val tags = " "
        val expected = emptyList<BrowseChipType>()
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }

    @Test
    fun `generate chips 7`() {
        val tags = "ordfav:Mikansei_GitHub"
        val expected = listOf(
            ordfavChip,
        )
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }

    @Test
    fun `generate chips 8`() {
        val tags = "(ebizuka_tomo or rupa_(girls_band_cry)) (highres or iseri)"
        val expected = listOf(
            BrowseChipType.Or(
                listOf(
                    tomoChip,
                    rupaChip,
                ),
            ),
            BrowseChipType.Or(
                listOf(
                    highresChip,
                    iseriChip,
                ),
            ),
        )
        val result = generateChipsFromTagsUseCase(tags, cachedTags)
        assert(result == expected) {
            "expected : $expected\nresult   : $result"
        }
    }
}
