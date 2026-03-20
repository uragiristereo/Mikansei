package com.uragiristereo.mikansei.feature.image.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerPool(
    private val playerBuilder: () -> ExoPlayer.Builder,
) {
    private val players = mutableMapOf<Int, ExoPlayer>()

    fun getPlayer(index: Int): ExoPlayer {
        val key = index % POOL_SIZE
        return players.getOrPut(key) { playerBuilder().build() }
    }

    fun releaseAll() {
        players.values.forEach { it.release() }
        players.clear()
    }

    companion object {
        private const val POOL_SIZE = 3
    }
}

@Composable
fun retainVideoPlayerPool(
    playerBuilder: () -> ExoPlayer.Builder,
): VideoPlayerPool {
    return retain {
        VideoPlayerPool(
            playerBuilder = playerBuilder,
        )
    }
}
