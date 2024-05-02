package com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.core

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.product.component.ProductTopAppBar
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun NewOrEditSavedSearchTopAppBar(
    savedSearchId: Int?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProductStatusBarSpacer {
        ProductTopAppBar(
            title = {
                Column {
                    Text(
                        text = when {
                            savedSearchId != null -> "Edit saved search"
                            else -> "New saved search"
                        },
                    )

                    if (savedSearchId != null) {
                        Text(
                            text = savedSearchId.toString(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.onSurface.copy(
                                alpha = ContentAlpha.medium,
                            ),
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = null,
                        )
                    },
                )
            },
//            actions = {
//                IconButton(
//                    onClick = {
//
//                    },
//                    content = {
//                        Icon(
//                            painter = painterResource(id = R.drawable.more_vert),
//                            contentDescription = null,
//                        )
//                    },
//                )
//            },
            modifier = modifier,
        )
    }
}
