package com.uragiristereo.mikansei.feature.image

import com.uragiristereo.mikansei.feature.image.image.ImageViewModel
import com.uragiristereo.mikansei.feature.image.more.MoreBottomSheetViewModel
import com.uragiristereo.mikansei.feature.image.video.VideoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object ImageModule {
    operator fun invoke(): Module = module {
        viewModelOf(::ImageViewModel)
        viewModelOf(::MoreBottomSheetViewModel)
        viewModelOf(::ViewerViewModel)
        viewModelOf(::ImageViewModel)
        viewModelOf(::VideoViewModel)
    }
}
