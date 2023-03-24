package com.uragiristereo.mikansei.feature.image

import com.uragiristereo.mikansei.feature.image.more.MoreBottomSheetViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object ImageModule {
    operator fun invoke(): Module = module {
        viewModelOf(::ImageViewModel)
        viewModelOf(::MoreBottomSheetViewModel)
    }
}
