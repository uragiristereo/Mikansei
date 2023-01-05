package com.uragiristereo.mejiboard.core.database.dao.session

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uragiristereo.mejiboard.domain.entity.booru.post.PostImage
import com.uragiristereo.mejiboard.domain.entity.booru.post.Rating
import java.util.Date

@Entity(tableName = "sessions")
data class PostSession(
    @PrimaryKey(autoGenerate = false)
    val uuid: String,

    val sessionId: String,
    val sequence: Int,
    val source: String,
    val id: Int,
    val scaled: Boolean,
    val rating: Rating,
    val tags: String,
    val uploadedAt: Date,
    val uploader: String,
    val imageSource: String,
    val originalImage: PostImage,
    val scaledImage: PostImage,
    val previewImage: PostImage,
    val aspectRatio: Float,
)
