package com.uragiristereo.mikansei.core.product.shared.postfavoritevote

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.coroutines.channels.Channel

interface PostFavoriteVote {
    val post: Post
    val isPostInFavorites: Boolean
    val favoriteCount: Int
    val scoreState: PostVote.Status
    val score: Int
    val isPostUpdated: Boolean
    val favoriteButtonEnabled: Boolean
    val voteButtonEnabled: Boolean
    val toastChannel: Channel<Pair<String, Int>>

    fun toggleFavorite(value: Boolean)
    fun upvotePost()
    fun downvotePost()
    fun unvotePost()
    fun onVoteChange(value: PostVote.Status)
}
