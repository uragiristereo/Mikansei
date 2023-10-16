package com.uragiristereo.mikansei.feature.home.posts.more

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVote
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVoteImpl
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import kotlinx.coroutines.flow.StateFlow

class PostMoreViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : ViewModel(), PostFavoriteVote by PostFavoriteVoteImpl() {
    override val post: Post = savedStateHandle.getData<HomeRoute.PostMore>()!!.post

    val activeUser: StateFlow<Profile>
        get() = userRepository.active
}
