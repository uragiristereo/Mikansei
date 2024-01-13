package com.uragiristereo.mikansei.feature.image.video

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf
import kotlin.math.floor

@androidx.annotation.OptIn(UnstableApi::class)
class VideoViewModel(
    savedStateHandle: SavedStateHandle,
    private val networkRepository: NetworkRepository,
    private val applicationContext: Context,
) : ViewModel() {
    val post = checkNotNull(savedStateHandle.navArgsOf<MainRoute.Image>()).post

    val exoPlayer = buildExoPlayer()

    var isPlaying by mutableStateOf(true); private set
    var isBuffering by mutableStateOf(true); private set
    var sliderValue by mutableStateOf(0f); private set
    var elapsed by mutableStateOf(0L); private set
    var total by mutableStateOf(0L); private set

    val sliderValueFmt by derivedStateOf { formatTime(sliderValue.toLong()) }
    val elapsedFmt by derivedStateOf { formatTime(elapsed) }
    val totalFmt by derivedStateOf { formatTime(total) }

    val offsetY = Animatable(initialValue = 0f)

    override fun onCleared() {
        super.onCleared()

        exoPlayer.release()
    }

    fun updatePosition(position: Long, total: Long) {
        elapsed = position
        this.total = total
    }

    fun onSeek(position: Float) {
        sliderValue = position
    }

    fun onJump() {
        elapsed = sliderValue.toLong()
    }

    fun onPlayPauseToggle(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    fun onPlaybackStateChange(isBuffering: Boolean) {
        this.isBuffering = isBuffering
    }

    private fun buildExoPlayer(): ExoPlayer {
        return ExoPlayer.Builder(applicationContext)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(),
                true,
            )
            .build()
            .apply {
                val mediaItem = MediaItem.fromUri(
                    when {
                        post.type == Post.Type.UGOIRA -> post.medias.scaled!!.url
                        else -> post.medias.original.url
                    }
                )

                setMediaItem(mediaItem)
                setMediaSource(
                    ProgressiveMediaSource
                        .Factory(networkRepository.exoPlayerCacheFactory)
                        .createMediaSource(mediaItem)
                )

                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = true
            }
    }

    private fun formatTime(value: Long): String {
        val minutes = floor(value.toDouble() / 60000).toInt()
        val seconds = "${((value - (minutes * 60000)) / 1000).toInt()}"
            .padStart(length = 2, padChar = '0')

        return "$minutes:$seconds"
    }
}
