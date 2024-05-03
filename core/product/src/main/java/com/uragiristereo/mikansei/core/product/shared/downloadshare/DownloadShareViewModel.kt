package com.uragiristereo.mikansei.core.product.shared.downloadshare

import android.content.Context
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import com.uragiristereo.mikansei.core.product.shared.downloadshare.core.DownloadState
import kotlinx.coroutines.flow.Flow

interface DownloadShareViewModel {
    val shareDialogVisible: Boolean
    val downloadState: DownloadState
    var selectedPost: Post?
    val downloadShareEvent: Flow<Event>

    fun downloadPost(post: Post)

    fun sharePost(
        context: Context,
        post: Post,
        shareOption: ShareOption,
    )

    fun cancelShare()

    enum class Event {
        DOWNLOADING,
        POST_IS_ALREADY_DOWNLOADING,
    }
}
