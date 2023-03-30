package com.uragiristereo.mikansei.core.database.dao.user

import com.uragiristereo.mikansei.core.model.danbooru.UserLevel
import com.uragiristereo.mikansei.core.model.danbooru.getUserLevelById
import com.uragiristereo.mikansei.core.model.user.User
import com.uragiristereo.mikansei.core.model.user.preference.DetailSizePreference
import com.uragiristereo.mikansei.core.model.user.preference.getEnumFromDanbooru

fun UserRow.toUser(): User {
    return User(
        id = id,
        name = name,
        apiKey = apiKey,
        level = UserLevel.values().getUserLevelById(level),
        safeMode = safeMode,
        showDeletedPosts = showDeletedPosts,
        defaultImageSize = DetailSizePreference.values().getEnumFromDanbooru(defaultImageSize),
        blacklistedTags = blacklistedTags.split(' '),
        isActive = isActive,
        postsRatingFilter = postsRatingFilter,
        blurQuestionablePosts = blurQuestionablePosts,
        blurExplicitPosts = blurExplicitPosts,
        nameAlias = getInitialChars(name),
    )
}

fun List<UserRow>.toUserList(): List<User> {
    return map { it.toUser() }
}

fun User.toUserRow(): UserRow {
    return UserRow(
        id = id,
        name = name,
        apiKey = apiKey,
        level = level.id,
        safeMode = safeMode,
        showDeletedPosts = showDeletedPosts,
        defaultImageSize = defaultImageSize.getEnumForDanbooru(),
        blacklistedTags = blacklistedTags.joinToString(separator = " "),
        isActive = isActive,
        postsRatingFilter = postsRatingFilter,
        blurQuestionablePosts = blurQuestionablePosts,
        blurExplicitPosts = blurExplicitPosts,
    )
}

fun List<User>.toUserRowList(): List<UserRow> {
    return map { it.toUserRow() }
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
