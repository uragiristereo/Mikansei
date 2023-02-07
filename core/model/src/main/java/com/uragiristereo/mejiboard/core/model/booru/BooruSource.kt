package com.uragiristereo.mejiboard.core.model.booru

import android.content.Context
import androidx.annotation.StringRes
import com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem
import com.uragiristereo.mejiboard.core.resources.R

enum class BooruSource(
    val key: String,
    @StringRes val nameResId: Int,
    @StringRes val domainResId: Int,
    private val webUrlPattern: String,
) {
    Gelbooru(
        key = "gelbooru",
        nameResId = R.string.gelbooru_label,
        domainResId = R.string.gelbooru_domain,
        webUrlPattern = "https://gelbooru.com/index.php?page=post&s=view&id={postId}",
    ),
    Danbooru(
        key = "danbooru",
        nameResId = R.string.danbooru_label,
        domainResId = R.string.danbooru_domain,
        webUrlPattern = "https://danbooru.donmai.us/posts/{postId}",
    ),
    SafebooruOrg(
        key = "safebooruorg",
        nameResId = R.string.safebooruorg_label,
        domainResId = R.string.safebooruorg_domain,
        webUrlPattern = "https://safebooru.org/index.php?page=post&s=view&id={postId}",
    ),
    Yandere(
        key = "yandere",
        nameResId = R.string.yandere_label,
        domainResId = R.string.yandere_domain,
        webUrlPattern = "https://yande.re/post/show/{postId}",
    );

    fun baseUrl(context: Context): String {
        val domain = context.getString(domainResId)

        return "https://$domain"
    }

    fun parseWebUrl(postId: Int): String {
        return this.webUrlPattern
            .replace(
                oldValue = "{postId}",
                newValue = postId.toString(),
            )
    }

    fun toPair(): Pair<String, BooruSource> {
        return this.key to this
    }

    fun toPreferenceItem(): PreferenceItem {
        return PreferenceItem(
            key = key,
            titleResId = nameResId,
            subtitleResId = domainResId,
        )
    }
}


fun Array<BooruSource>.getBooruByKey(key: String): BooruSource? {
    return this.firstOrNull { it.key == key }
}
