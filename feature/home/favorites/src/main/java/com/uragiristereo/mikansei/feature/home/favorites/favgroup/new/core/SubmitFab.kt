package com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun SubmitFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.done),
                contentDescription = null,
            )
        },
        modifier = modifier,
    )
}
