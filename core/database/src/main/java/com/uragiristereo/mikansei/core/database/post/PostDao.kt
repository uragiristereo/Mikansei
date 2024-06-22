package com.uragiristereo.mikansei.core.database.post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PostDao {
    @Query("select data from posts")
    suspend fun getPosts(): List<Post>

    @Query("select uploader_name from posts where post_id = :postId")
    fun getUploaderName(postId: Int): Flow<String?>

    @Insert
    suspend fun add(item: PostRow)

    @Query(
        """
            update posts set data = :post, updated_at = :updatedAt
            where post_id = :postId
        """
    )
    suspend fun update(postId: Int, post: Post, updatedAt: Date = Date())

    @Transaction
    suspend fun update(posts: List<Post>) {
        val existingPostIds = getPosts()
            .map { it.id }
            .associateWith { true }

        posts.forEach { post ->
            if (!existingPostIds.containsKey(post.id)) {
                add(post.toPostRow())
            } else {
                update(postId = post.id, post = post)
            }
        }
    }

    @Query(
        """
            update posts set uploader_name = :uploaderName, updated_at = :updatedAt
            where post_id = :postId
        """
    )
    suspend fun updateUploaderName(
        postId: Int,
        uploaderName: String,
        updatedAt: Date = Date(),
    )

    @Query("delete from posts")
    suspend fun reset()
}
