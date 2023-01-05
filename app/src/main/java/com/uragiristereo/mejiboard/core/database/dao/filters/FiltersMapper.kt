package com.uragiristereo.mejiboard.core.database.dao.filters

import com.uragiristereo.mejiboard.core.common.ui.entity.FilterItem

fun FilterTableItem.toFilterItem(): FilterItem {
    return FilterItem(
        id = id,
        tag = tag,
        enabled = enabled,
        selected = false,
    )
}

fun List<FilterTableItem>.toFilterItemList(): List<FilterItem> {
    return map { it.toFilterItem() }
}

fun FilterItem.toFilterTableItem(): FilterTableItem {
    return FilterTableItem(
        id = id,
        tag = tag,
        enabled = enabled,
    )
}

fun List<FilterItem>.toFilterTableItemList(): List<FilterTableItem> {
    return map { it.toFilterTableItem() }
}
