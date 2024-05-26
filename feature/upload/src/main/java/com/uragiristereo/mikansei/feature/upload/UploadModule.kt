package com.uragiristereo.mikansei.feature.upload

import com.uragiristereo.mikansei.core.ui.dfm.DynamicComposable
import com.uragiristereo.mikansei.core.ui.dfm.KoinDynamicContext
import com.uragiristereo.mikansei.core.ui.dfm.koinSharedNavViewModel
import com.uragiristereo.mikansei.feature.upload.choose_media.ChooseMediaScreen
import com.uragiristereo.mikansei.feature.upload.data.UploadRepository
import com.uragiristereo.mikansei.feature.upload.select_media.SelectMediaScreen
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uploadModule = module {
    viewModelOf(::UploadViewModel)
    singleOf(::UploadRepository)
}

val chooseMediaScreen = DynamicComposable { navController ->
    KoinDynamicContext(module = uploadModule) {
        val uploadViewModel: UploadViewModel = koinSharedNavViewModel(
            navController = navController,
            parentRoute = "upload",
        )

        ChooseMediaScreen(
            onNext = {
                navController.navigate("select_media")
            },
            uploadViewModel = uploadViewModel,
        )
    }
}

val selectMediaScreen = DynamicComposable { navController ->
    KoinDynamicContext(module = uploadModule) {
        val uploadViewModel: UploadViewModel = koinSharedNavViewModel(
            navController = navController,
            parentRoute = "upload",
        )

        SelectMediaScreen(
            onNext = {},
            uploadViewModel = uploadViewModel,
        )
    }
}
