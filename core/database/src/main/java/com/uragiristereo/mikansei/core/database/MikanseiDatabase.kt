package com.uragiristereo.mikansei.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uragiristereo.mikansei.core.database.member.MemberDao
import com.uragiristereo.mikansei.core.database.member.MemberRow
import com.uragiristereo.mikansei.core.database.migration.From1To2MigrationSpec
import com.uragiristereo.mikansei.core.database.post.PostDao
import com.uragiristereo.mikansei.core.database.post.PostRow
import com.uragiristereo.mikansei.core.database.post_favorite_vote.PostFavoriteVoteDao
import com.uragiristereo.mikansei.core.database.post_favorite_vote.PostFavoriteVoteRow
import com.uragiristereo.mikansei.core.database.session.SessionDao
import com.uragiristereo.mikansei.core.database.session.SessionRow
import com.uragiristereo.mikansei.core.database.session_post.SessionPostDao
import com.uragiristereo.mikansei.core.database.session_post.SessionPostRow
import com.uragiristereo.mikansei.core.database.tag_category.TagCategoryDao
import com.uragiristereo.mikansei.core.database.tag_category.TagCategoryRow
import com.uragiristereo.mikansei.core.database.user.UserDao
import com.uragiristereo.mikansei.core.database.user.UserRow
import com.uragiristereo.mikansei.core.database.user_delegation.UserDelegationDao
import com.uragiristereo.mikansei.core.database.user_delegation.UserDelegationRow

@Database(
    entities = [
        SessionRow::class,
        UserRow::class,
        UserDelegationRow::class,
        PostRow::class,
        SessionPostRow::class,
        TagCategoryRow::class,
        PostFavoriteVoteRow::class,
        MemberRow::class,
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = From1To2MigrationSpec::class),
        AutoMigration(from = 2, to = 3),
    ],
)
@TypeConverters(DatabaseConverters::class)
abstract class MikanseiDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    abstract fun userDao(): UserDao

    abstract fun searchDelegationDao(): UserDelegationDao

    abstract fun postDao(): PostDao

    abstract fun sessionPostDao(): SessionPostDao

    abstract fun tagCategoryDao(): TagCategoryDao

    abstract fun postFavoriteVoteDao(): PostFavoriteVoteDao

    abstract fun memberDao(): MemberDao
}
