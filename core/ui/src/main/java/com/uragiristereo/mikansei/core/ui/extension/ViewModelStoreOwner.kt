package com.uragiristereo.mikansei.core.ui.extension

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController

@Composable
fun rememberParentViewModelStoreOwner(
    navController: NavHostController,
    parentRoute: String,
): ViewModelStoreOwner {
    return remember(navController, parentRoute) {
        object : ViewModelStoreOwner {
            @SuppressLint("UnrememberedGetBackStackEntry")
            override val viewModelStore = navController
                .getBackStackEntry(parentRoute)
                .viewModelStore
        }
    }
}
