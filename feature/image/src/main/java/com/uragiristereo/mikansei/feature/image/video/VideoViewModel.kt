package com.uragiristereo.mikansei.feature.image.video

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.floor

@androidx.annotation.OptIn(UnstableApi::class)
class VideoViewModel(
    private val preferencesRepository: PreferencesRepository,
    val post: Post,
) : ViewModel() {
    val noSound = post.tags.none { it == "sound" }

    val mediaItem = MediaItem.fromUri(
        when {
            post.type == Post.Type.UGOIRA -> post.medias.scaled!!.url
            else -> post.medias.original.url
        }
    )

    var isBuffering by mutableStateOf(true); private set
    var sliderValue by mutableStateOf(0f); private set
    var elapsed by mutableStateOf(0L); private set
    var total by mutableStateOf(0L); private set

    val sliderValueFmt by derivedStateOf { formatTime(sliderValue.toLong()) }
    val elapsedFmt by derivedStateOf { formatTime(elapsed) }
    val totalFmt by derivedStateOf { formatTime(total) }

    val videoMuted = preferencesRepository.data
        .map { it.videoMuted }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = preferencesRepository.data.value.videoMuted,
        )

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

    fun onPlaybackStateChange(isBuffering: Boolean) {
        this.isBuffering = isBuffering
    }

    fun onToggleVideoMuted() {
        viewModelScope.launch {
            preferencesRepository.update {
                it.copy(videoMuted = !it.videoMuted)
            }
        }
    }

    private fun formatTime(value: Long): String {
        val minutes = floor(value.toDouble() / 60000).toInt()
        val seconds = "${((value - (minutes * 60000)) / 1000).toInt()}"
            .padStart(length = 2, padChar = '0')

        return "$minutes:$seconds"
    }
}
