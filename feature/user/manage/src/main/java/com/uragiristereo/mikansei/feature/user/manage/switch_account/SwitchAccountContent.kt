package com.uragiristereo.mikansei.feature.user.manage.switch_account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.ui.composable.SectionTitle
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.bottomSheetContentPadding
import com.uragiristereo.mikansei.feature.user.manage.ManageUserViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SwitchAccountContent(
    onDismiss: suspend () -> Unit,
    onNavigateToManage: () -> Unit,
    viewModel: ManageUserViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()

    val allUsers by viewModel.allUsers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .bottomSheetContentPadding(),
    ) {
        SectionTitle(
            text = "Switch account",
            fontSize = 18.sp,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
        )

        Divider()

        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
        ) {
            allUsers.forEach { item ->
                SwitchAccountProfileItem(
                    user = item,
                    onActivateClick = {
                        scope.launch {
                            onDismiss()
                            viewModel.switchActiveUser(it)
                        }
                    },
                )
            }
        }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
