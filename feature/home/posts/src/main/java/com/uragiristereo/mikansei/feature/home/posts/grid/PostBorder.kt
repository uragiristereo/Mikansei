package com.uragiristereo.mikansei.feature.home.posts.grid

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme

@Composable
fun PostBorder(
    status: Post.Status,
    relationshipType: Post.RelationshipType,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val colors = generateColors(status, relationshipType)
        val isPostHasBorder = colors.bottomRight != null && colors.top != null && colors.left != null

        Box(
            modifier = when {
                isPostHasBorder -> Modifier.padding(all = BorderWidth)
                else -> Modifier
            },
            content = content,
        )

        RoundedCornerBorder(
            color = colors.bottomRight ?: Color.Transparent,
            clipShape = BottomRightBorderShape,
        )

        RoundedCornerBorder(
            color = colors.left ?: Color.Transparent,
            clipShape = LeftBorderShape,
        )

        RoundedCornerBorder(
            color = colors.top ?: Color.Transparent,
            clipShape = TopBorderShape,
        )
    }
}


@Composable
private fun RoundedCornerBorder(
    color: Color,
    clipShape: Shape,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(clipShape)
            .border(
                width = BorderWidth,
                color = color,
                shape = RoundedCornerShape(8.dp),
            ),
    )
}

@Composable
private fun generateColors(
    status: Post.Status,
    relationshipType: Post.RelationshipType,
): BorderColor {
    return when (relationshipType) {
        Post.RelationshipType.PARENT_CHILD -> when (status) {
            Post.Status.PENDING -> BorderColor(PendingColor, ParentColor, ChildColor)
            Post.Status.DELETED, Post.Status.BANNED -> BorderColor(DeletedBannedColor, ParentColor, ChildColor)
            Post.Status.ACTIVE -> BorderColor(ChildColor, ParentColor, ParentColor)
        }

        Post.RelationshipType.PARENT -> when (status) {
            Post.Status.PENDING -> BorderColor(PendingColor, ParentColor, ParentColor)
            Post.Status.DELETED, Post.Status.BANNED -> BorderColor(DeletedBannedColor, ParentColor, ParentColor)
            Post.Status.ACTIVE -> BorderColor(ParentColor, ParentColor, ParentColor)
        }

        Post.RelationshipType.CHILD -> when (status) {
            Post.Status.PENDING -> BorderColor(PendingColor, ChildColor, ChildColor)
            Post.Status.DELETED, Post.Status.BANNED -> BorderColor(DeletedBannedColor, ChildColor, ChildColor)
            Post.Status.ACTIVE -> BorderColor(ChildColor, ChildColor, ChildColor)
        }

        Post.RelationshipType.NONE -> when (status) {
            Post.Status.PENDING -> BorderColor(PendingColor, PendingColor, PendingColor)
            Post.Status.DELETED, Post.Status.BANNED -> BorderColor(DeletedBannedColor, DeletedBannedColor, DeletedBannedColor)
            Post.Status.ACTIVE -> BorderColor(null, null, null)
        }
    }
}

data class BorderColor(
    val bottomRight: Color?,
    val top: Color?,
    val left: Color?,
)

private val BorderWidth = 2.dp
private val PendingColor = Color(0xFF0095DD)
private val DeletedBannedColor = Color(0xFFABABBC)
private val ParentColor = Color(0xFF30B443)
private val ChildColor = Color(0xFFFD9200)

private val BottomRightBorderShape = GenericShape { size, _ ->
    moveTo(size.width, 0f)
    lineTo(0f, size.height)
    lineTo(size.width, size.height)
}

private val TopBorderShape = GenericShape { size, _ ->
    moveTo(size.width, 0f)
    lineTo(size.width / 2f, size.height / 2f)
    lineTo(0f, 0f)
}

private val LeftBorderShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width / 2f, size.height / 2f)
    lineTo(0f, size.height)
}

@Preview
@Composable
private fun PostBorderPreview() {
    val modifierSize = Modifier.size(
        width = 40.dp,
        height = 30.dp,
    )

    MikanseiTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                PostBorder(
                    status = Post.Status.DELETED,
                    relationshipType = Post.RelationshipType.NONE,
                    modifier = modifierSize,
                    content = {},
                )
                PostBorder(
                    status = Post.Status.DELETED,
                    relationshipType = Post.RelationshipType.PARENT,
                    modifier = modifierSize,
                    content = {},
                )
                PostBorder(
                    status = Post.Status.PENDING,
                    relationshipType = Post.RelationshipType.CHILD,
                    modifier = modifierSize,
                    content = {},
                )

                PostBorder(
                    status = Post.Status.ACTIVE,
                    relationshipType = Post.RelationshipType.PARENT_CHILD,
                    modifier = modifierSize,
                    content = {},
                )

                PostBorder(
                    status = Post.Status.PENDING,
                    relationshipType = Post.RelationshipType.PARENT_CHILD,
                    modifier = modifierSize,
                    content = {},
                )

                PostBorder(
                    status = Post.Status.PENDING,
                    relationshipType = Post.RelationshipType.NONE,
                    modifier = modifierSize,
                    content = {},
                )
            }
        }
    }
}
