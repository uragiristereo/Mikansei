package com.uragiristereo.mejiboard.common.helper

import com.uragiristereo.mejiboard.domain.entity.source.post.PostImage

object ImageHelper {
    fun resizeImage(postImage: PostImage): Pair<Int, Int> {
        val maxSize = 4096f

        return when {
            postImage.width > maxSize || postImage.height > maxSize -> {
                val scale = when {
                    postImage.width > postImage.height -> maxSize.div(postImage.width)
                    else -> maxSize.div(postImage.height)
                }

                val scaledWidth = postImage.width * scale
                val scaledHeight = postImage.height * scale

                Pair(scaledWidth.toInt(), scaledHeight.toInt())
            }

            else -> Pair(postImage.width, postImage.height)
        }
    }
}
