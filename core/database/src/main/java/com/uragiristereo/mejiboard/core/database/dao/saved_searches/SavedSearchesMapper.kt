package com.uragiristereo.mejiboard.core.database.dao.saved_searches

import com.uragiristereo.mejiboard.core.common.ui.entity.SavedSearchItem

fun SavedSearchTableItem.toSavedSearchItem(): SavedSearchItem {
    return SavedSearchItem(
        id = id,
        tag = tag,
    )
}

fun List<SavedSearchTableItem>.toSavedSearchItemList(): List<SavedSearchItem> {
    return map { it.toSavedSearchItem() }
}

fun SavedSearchItem.toSavedSearchTableItem(): SavedSearchTableItem {
    return SavedSearchTableItem(
        id = id,
        tag = tag,
    )
}

fun List<SavedSearchItem>.toSavedSearchTableItemList(): List<SavedSearchTableItem> {
    return map { it.toSavedSearchTableItem() }
}
