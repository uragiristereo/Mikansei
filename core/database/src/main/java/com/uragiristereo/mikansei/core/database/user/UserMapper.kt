package com.uragiristereo.mikansei.core.database.user

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.model.preferences.user.getEnumFromDanbooru

fun UserRow.toProfile(): Profile {
    return Profile(
        id = id,
        name = name,
        apiKey = apiKey,
        level = Profile.Level.getLevelById(level),
        danbooru = Profile.DanbooruSettings(
            safeMode = safeMode,
            showDeletedPosts = showDeletedPosts,
            defaultImageSize = DetailSizePreference.entries.getEnumFromDanbooru(defaultImageSize),
            blacklistedTags = when {
                blacklistedTags.isNotBlank() -> blacklistedTags.split('\n').distinct()
                else -> listOf()
            },
        ),
        mikansei = Profile.MikanseiSettings(
            // TODO: is prod account
            isActive = isActive,
            postsRatingFilter = postsRatingFilter,
            blurQuestionablePosts = blurQuestionablePosts,
            blurExplicitPosts = blurExplicitPosts,
            nameAlias = getInitialChars(name),
        ),
    )
}

fun List<UserRow>.toProfileList(): List<Profile> {
    return map { it.toProfile() }
}

fun Profile.toUserRow(): UserRow {
    return UserRow(
        id = id,
        name = name,
        apiKey = apiKey,
        level = level.id,
        safeMode = danbooru.safeMode,
        showDeletedPosts = danbooru.showDeletedPosts,
        defaultImageSize = danbooru.defaultImageSize.getEnumForDanbooru(),
        blacklistedTags = danbooru.blacklistedTags.joinToString("\n"),
        postsRatingFilter = mikansei.postsRatingFilter,
        isActive = mikansei.isActive,
    )
}

private fun getInitialChars(word: String): String {
    val result = StringBuilder()
    var previousChar = ' '

    word.forEach { char ->
        when {
            result.length == 2 -> return@forEach
            char.isUpperCase() || char.isDigit() -> result.append(char)
            previousChar == '_' || previousChar == '-' -> result.append(char)
        }

        previousChar = char
    }

    when {
        result.isEmpty() -> result.append(word.first())
        result.count() == 1 && (word.any { it == '_' || it == '-' }) -> result.insert(0, word.first())
    }

    return result.toString().uppercase()
}
