package com.uragiristereo.mikansei.core.model.danbooru

enum class DanbooruHost {
    Danbooru,
    Safebooru,
    Testbooru,
    DonmaiMoe;
//    HIJIRIBE,
//    SONOHARA,
//    SHIMA,
//    SAITOU,
//    KAGAMIHARA;

    fun getBaseUrl(): String {
        return when (this) {
            DonmaiMoe -> "https://donmai.moe"
            else -> "https://${this.name.lowercase()}.donmai.us"
        }
    }

    fun parsePostLink(postId: Int): String {
        return "${getBaseUrl()}/posts/$postId"
    }
}
