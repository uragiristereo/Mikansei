package com.uragiristereo.mikansei.core.model.dagger.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified T : ViewModel> assistedViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    extras: CreationExtras = when (viewModelStoreOwner) {
        is HasDefaultViewModelProviderFactory -> viewModelStoreOwner.defaultViewModelCreationExtras
        else -> CreationExtras.Empty
    },
    crossinline factory: () -> ViewModelFactory<T>,
): T {
    return viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        key = key,
        extras = extras,
        factory = GenericViewModelFactory(factory()),
    )
}
