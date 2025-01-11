package com.uragiristereo.mikansei.core.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
fun NavController.rememberParentNavBackStackEntry(): NavBackStackEntry {
    return remember(currentBackStackEntry) {
        val destination = currentBackStackEntry?.destination
        val parentId = requireNotNull(destination?.parent?.id) {
            error("Destination id=${destination?.id} route=${destination?.route} doesn't have a parent")
        }
        getBackStackEntry(parentId)
    }
}
