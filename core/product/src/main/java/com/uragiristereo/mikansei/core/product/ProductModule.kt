package com.uragiristereo.mikansei.core.product

import com.uragiristereo.mikansei.core.product.shared.downloadshare.DownloadShareViewModel
import com.uragiristereo.mikansei.core.product.shared.downloadshare.DownloadShareViewModelImpl
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

object ProductModule {
    operator fun invoke(): Module = module {
        viewModelOf(::DownloadShareViewModelImpl) { bind<DownloadShareViewModel>() }
        viewModel { parameters ->
            PostFavoriteVoteViewModel(
                postId = parameters.get(),
                danbooruRepository = get(),
                postRepository = get(),
                postFavoriteVoteRepository = get(),
                userRepository = get(),
                getPostWithFavoriteVoteUseCase = get(),
            )
        }
    }
}
