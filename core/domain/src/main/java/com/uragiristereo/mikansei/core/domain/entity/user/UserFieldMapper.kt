package com.uragiristereo.mikansei.core.domain.entity.user

import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserField
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserFieldData

fun UserField.toDanbooruUserField(): DanbooruUserField {
    return DanbooruUserField(
        user = DanbooruUserFieldData(
            enableSafeMode = safeMode,
            showDeletedPosts = showDeletedPosts,
            defaultImageSize = defaultImageSize?.getEnumForDanbooru(),
            blacklistedTags = blacklistedTags?.joinToString(separator = " "),
        )
    )
}
