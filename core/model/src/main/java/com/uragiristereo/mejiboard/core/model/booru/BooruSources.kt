package com.uragiristereo.mejiboard.core.model.booru

import com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem
import com.uragiristereo.mejiboard.core.resources.R

object BooruSources {
    val Gelbooru = BooruSource(
        key = "gelbooru",
        nameResId = R.string.gelbooru_label,
        domainResId = R.string.gelbooru_domain,
        webUrlPattern = "https://gelbooru.com/index.php?page=post&s=view&id={postId}",
    )
    val Danbooru = BooruSource(
        key = "danbooru",
        nameResId = R.string.danbooru_label,
        domainResId = R.string.danbooru_domain,
        webUrlPattern = "https://danbooru.donmai.us/posts/{postId}",
    )
    val SafebooruOrg = BooruSource(
        key = "safebooruorg",
        nameResId = R.string.safebooruorg_label,
        domainResId = R.string.safebooruorg_domain,
        webUrlPattern = "https://safebooru.org/index.php?page=post&s=view&id={postId}",
    )
    val Yandere = BooruSource(
        key = "yandere",
        nameResId = R.string.yandere_label,
        domainResId = R.string.yandere_domain,
        webUrlPattern = "https://yande.re/post/show/{postId}",
    )

    val list = listOf(
        Gelbooru,
        Danbooru,
        SafebooruOrg,
        Yandere,
    )

    val map = list.associate { it.toPair() }

    fun getBooruByKey(key: String): BooruSource? {
        return map[key]
    }

    fun toPreferenceItemList(): List<PreferenceItem> {
        return list.map { it.toPreferenceItem() }
    }
}
