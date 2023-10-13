package com.uragiristereo.mikansei.feature.user.manage.switch_account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.ui.composable.DragHandle
import com.uragiristereo.mikansei.core.ui.composable.SectionTitle
import com.uragiristereo.mikansei.feature.user.manage.ManageUserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SwitchAccountContent(
    onNavigateBack: () -> Unit,
    onNavigateToManage: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ManageUserViewModel = koinViewModel(),
) {
    val allUsers by viewModel.allUsers.collectAsState()

    LazyColumn(
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier.consumeWindowInsets(WindowInsets.navigationBars),
    ) {
        item {
            DragHandle()
        }

        item {
            SectionTitle(
                text = "Switch account",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            )
        }

        item {
            Divider(
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        items(
            items = allUsers,
            key = { it.id },
        ) { user ->
            SwitchAccountProfileItem(
                user = user,
                onActivateClick = {
                    viewModel.switchActiveUser(it)
                    onNavigateBack()
                },
            )
        }

        item {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            ) {
                TextButton(
                    onClick = onNavigateToManage,
                    content = {
                        Text(text = "Manage Accounts".uppercase())
                    },
                )
            }
        }
    }
}
