package com.uragiristereo.mikansei.core.ui.dfm

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.extension.rememberParentViewModelStoreOwner
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
inline fun <reified T : ViewModel> koinSharedNavViewModel(
    navController: NavHostController,
    parentRoute: String
): T {
    val parentViewModelStoreOwner = rememberParentViewModelStoreOwner(
        navController = navController,
        parentRoute = parentRoute,
    )

    return koinNavViewModel(viewModelStoreOwner = parentViewModelStoreOwner)
}
