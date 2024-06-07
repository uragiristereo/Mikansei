package com.uragiristereo.mikansei.core.model.dagger.feature

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
inline fun <reified T : FeatureContainer> rememberContainer(
    crossinline block: @DisallowComposableCalls (Context) -> T,
): T {
    val context = LocalContext.current

    return remember { block(context) }
}
