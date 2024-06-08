package com.uragiristereo.mikansei.feature.about

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.about.data.AboutRepositoryImpl
import com.uragiristereo.mikansei.feature.about.domain.AboutRepository
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.bind
import org.koin.dsl.module

val aboutModule = module {
    viewModelOf(::AboutViewModel)

    scope<AboutViewModel> {
        scopedOf(::AboutRepositoryImpl) bind AboutRepository::class
    }
}

fun NavGraphBuilder.aboutRoute(
    navController: NavHostController,
) {
    composable<MainRoute.About> {
        AboutScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}
