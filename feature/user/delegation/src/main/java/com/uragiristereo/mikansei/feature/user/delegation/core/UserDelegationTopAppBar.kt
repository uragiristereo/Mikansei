package com.uragiristereo.mikansei.feature.user.delegation.core

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
fun UserDelegationTopAppBar(
    activeUserName: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProductStatusBarSpacer {
        ProductTopAppBar(
            title = {
                Column {
                    Text(text = "Delegate user")

                    Text(
                        text = activeUserName,
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                    )
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
            modifier = modifier,
        )
    }
}
