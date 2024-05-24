package com.uragiristereo.mikansei.feature.user.delegation.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mikansei.core.ui.composable.SettingTip

@Composable
fun UserDelegationList(
    items: List<UserDelegation>,
    contentPadding: PaddingValues,
    onItemClick: (UserDelegation) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            SettingTip(
                text = "Delegate accessing to some of Danbooru features (such as getting posts) to the selected user.",
            )
        }

        item {
            Divider()
        }

        items(
            items = items,
            key = { item -> item.userId },
        ) { item ->
            UserDelegationListItem(
                item = item,
                onClick = onItemClick,
            )
        }
    }
}
