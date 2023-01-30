package com.uragiristereo.mejiboard.feature.home.posts

//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun PostsNavGraph(
//    onNavigateMain: (MainRoute) -> Unit,
//    onNavigateImage: (Post) -> Unit,
//    onCurrentTagsChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    AnimatedNavHost(
//        navController = LocalPostsNavController.current,
//        startDestination = PostsRoute.Index(),
//        enterTransition = {
//            slideIntoContainer(
//                towards = AnimatedContentScope.SlideDirection.Right,
//                animationSpec = tween(durationMillis = 350),
//            )
//        },
//        exitTransition = {
//            slideOutOfContainer(
//                towards = AnimatedContentScope.SlideDirection.Right,
//                animationSpec = tween(durationMillis = 350),
//            )
//        },
//        popEnterTransition = {
//            slideIntoContainer(
//                towards = AnimatedContentScope.SlideDirection.Right,
//                animationSpec = tween(durationMillis = 350),
//            )
//        },
//        popExitTransition = {
//            slideOutOfContainer(
//                towards = AnimatedContentScope.SlideDirection.Right,
//                animationSpec = tween(durationMillis = 350),
//            )
//        },
//        modifier = modifier,
//    ) {
//        composable(
//            route = PostsRoute.Index(),
//            disableDeserialization = true,
//            content = {
//                PostsScreen(
//                    onNavigate = onNavigateMain,
//                    onNavigateImage = onNavigateImage,
//                    onCurrentTagsChange = onCurrentTagsChange,
//                )
//            },
//        )
//    }
//}
