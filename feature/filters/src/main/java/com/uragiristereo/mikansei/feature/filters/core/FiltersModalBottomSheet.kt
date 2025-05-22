package com.uragiristereo.mikansei.feature.filters.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.composable.ClickableSection
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.ModalBottomSheet3
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetState3

@Composable
internal fun FiltersModalBottomSheet(
    sheetState: SheetState3,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet3(
        sheetState = sheetState,
        modifier = modifier,
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text(
                text = stringResource(id = R.string.delete_confirmation_message),
                style = MaterialTheme.typography.caption,
                fontSize = 14.sp,
                color = MaterialTheme.colors.onBackground
                    .copy(alpha = ContentAlpha.medium),
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                    ),
            )

            ClickableSection(
                title = stringResource(id = R.string.delete_confirm),
                icon = painterResource(id = R.drawable.delete),
                onClick = onDeleteClick,
                modifier = Modifier
                    .windowInsetsPadding(
                        insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Bottom),
                    ),
            )
        }
    }
}
