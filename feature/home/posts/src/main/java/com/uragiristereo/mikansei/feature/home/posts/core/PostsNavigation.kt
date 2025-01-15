package com.uragiristereo.mikansei.feature.home.posts.core

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import com.uragiristereo.mikansei.core.ui.navigation.routeOf
import com.uragiristereo.mikansei.feature.home.posts.more.PostMoreContent
import com.uragiristereo.mikansei.feature.home.posts.share.ShareContent
import timber.log.Timber
import java.util.UUID

@SuppressLint("RestrictedApi")
fun NavGraphBuilder.postsRoute(
    mainNavController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
) {
    val lambdaOnNavigateImage: (Post) -> Unit = { item ->
        onNavigatedBackByGesture(false)

        mainNavController.navigate(
            MainRoute.Image(post = item)
        )
    }

    composable<HomeRoute.Posts>(
        enterTransition = {
            when (initialState.destination.id) {
                routeOf<HomeRoute.Posts>() -> slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 350),
                )

                else -> null
            }

        },
        exitTransition = {
            when (targetState.destination.id) {
                routeOf<HomeRoute.Posts>() -> slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 350),
                )

                else -> null
            }
        },
    ) { entry ->
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val isRouteFirstEntry = remember {
            // 3 from list of null, MainRoute.Home, HomeRoute.Posts
            mainNavController.currentBackStack.value.size == 3
        }

        val args = entry.toRoute<HomeRoute.Posts>()

        LaunchedEffect(key1 = args.tags) {
            onCurrentTagsChange(args.tags)
        }

        InterceptBackGestureForBottomSheetNavigator()

        val pagerViewModel = viewModel<PagerViewModel>()
        val pagerState = rememberPagerState {
            pagerViewModel.owners.size
        }
        val saveableStateHolder = rememberSaveableStateHolder()

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
        ) { index ->
            saveableStateHolder.SaveableStateProvider(index) {
                val id = rememberSaveable { UUID.randomUUID().toString() }
                val owner = pagerViewModel.owners[index]

                PagerContent(
                    postId = index,
                    id = id,
                    viewModel = viewModel(viewModelStoreOwner = owner),
                )
            }
        }
    }
}

@Composable
fun PagerContent(
    postId: Int,
    id: String,
    viewModel: PagerItemViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text("VM ${viewModel.id}")
        Text("Saveable $id")
        Text("Post $postId")
    }
}

class PagerViewModel : ViewModel() {
    val owners = listOf(
        generate(),
        generate(),
        generate(),
        generate(),
    )

    fun generate(): ViewModelStoreOwner {
        return object : ViewModelStoreOwner {
            override val viewModelStore = ViewModelStore()
        }
    }
}

class PagerItemViewModel : ViewModel() {
    val id = UUID.randomUUID().toString()

    init {
        Timber.d("invoked $id")
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.postsBottomRoute(
    mainNavController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
) {
    composable<HomeRoute.PostMore>(PostNavType) {
        val lambdaOnDownload = LocalLambdaOnDownload.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        PostMoreContent(
            onDismiss = bottomSheetNavigator.bottomSheetState::hide,
            onPostClick = { post ->
                bottomSheetNavigator.runHiding {
                    onNavigatedBackByGesture(false)

                    mainNavController.navigate(
                        MainRoute.Image(post)
                    )
                }
            },
            onDownloadClick = lambdaOnDownload,
            onShareClick = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.Share(post))
                }
            },
            onAddToFavoriteGroupClick = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.AddToFavGroup(post))
                }
            },
        )
    }

    composable<HomeRoute.Share>(PostNavType) {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val lambdaOnShare = LocalLambdaOnShare.current

        ShareContent(
            onDismiss = bottomSheetNavigator.bottomSheetState::hide,
            onPostClick = { post ->
                bottomSheetNavigator.runHiding {
                    onNavigatedBackByGesture(false)

                    mainNavController.navigate(
                        MainRoute.Image(post)
                    )
                }
            },
            onShareClick = lambdaOnShare,
        )
    }
}
