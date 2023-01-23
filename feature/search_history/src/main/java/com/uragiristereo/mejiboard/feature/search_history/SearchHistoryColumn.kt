package com.uragiristereo.mejiboard.feature.search_history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mejiboard.core.ui.composable.NavigationBarSpacer
import com.uragiristereo.mejiboard.core.ui.composable.SettingTip
import com.uragiristereo.mejiboard.core.ui.composable.SettingToggle

@Composable
fun SearchHistoryColumn(
    contentPadding: PaddingValues,
    toggleChecked: Boolean,
    onToggleChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        item {
            SettingToggle(
                checked = toggleChecked,
                onCheckedChange = onToggleChecked,
            )
        }

        item {
            SettingTip(
                text = "If search history turned off, your recent searches won’t be recorded. But your previous searches will stay saved.",
            )
        }

        item {
            NavigationBarSpacer()
        }
    }
}
