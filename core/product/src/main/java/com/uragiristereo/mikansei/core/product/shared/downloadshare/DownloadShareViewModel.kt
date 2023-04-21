package com.uragiristereo.mikansei.core.product.shared.downloadshare

import android.content.Context
import com.uragiristereo.mikansei.core.model.ShareOption
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.product.shared.downloadshare.core.DownloadState

interface DownloadShareViewModel {
    val shareDialogVisible: Boolean
    val downloadState: DownloadState
    var selectedPost: Post?

    fun downloadPost(context: Context, post: Post)

    fun sharePost(
        context: Context,
        post: Post,
        shareOption: ShareOption,
    )

    fun cancelShare()
}
