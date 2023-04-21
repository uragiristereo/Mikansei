package com.uragiristereo.mikansei.feature.home.posts.post_dialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVote
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVoteImpl
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute

class PostDialogViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), PostFavoriteVote by PostFavoriteVoteImpl() {
    override val post: Post = savedStateHandle.getData<HomeRoute.PostDialog>()!!.post
}
