package com.uragiristereo.mikansei.feature.image.more

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.database.MemberRepository
import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.domain.module.database.TagCategoryRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetMemberUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class MoreBottomSheetViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val networkRepository: NetworkRepository,
    userRepository: UserRepository,
    postRepository: PostRepository,
    memberRepository: MemberRepository,
    private val tagCategoryRepository: TagCategoryRepository,
    private val getTagsUseCase: GetTagsUseCase,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
    private val getMemberUseCase: GetMemberUseCase,
) : ViewModel() {
    private val args = savedStateHandle.toRoute<MainRoute.More>(PostNavType)
    private val postId = args.postId

    val post = postRepository.getPost(postId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uploaderName = post.flatMapLatest { post ->
        if (post != null) {
            getUploaderName(post.uploaderId)
            memberRepository.get(post.uploaderId).map {
                it?.name
            }
        } else {
            flowOf()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    var infoExpanded by mutableStateOf(false)
    var tagsExpanded by mutableStateOf(false)

    var loading by mutableStateOf(false)
    val tags = mutableStateListOf<Tag>()

    var scaledImageFileSizeStr by mutableStateOf("")
    var originalImageFileSizeStr by mutableStateOf("")

    private val customTabsIntentBuilder = CustomTabsIntent.Builder()

    val activeUser = userRepository.active

    fun getTags() {
        tagsExpanded = true
        val post = post.value

        if (tags.isEmpty() && post != null) {
            viewModelScope.launch {
                loading = true
                when (val result = getTagsUseCase(tags = post.tags)) {
                    is Result.Success -> {
                        tags.addAll(result.data)

                        val tagsMap = tags.associate { it.name to it.category }
                        tagCategoryRepository.update(tagsMap)
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }

                loading = false
            }
        }
    }

    fun getImagesFileSize() {
        val post = post.value ?: return

        viewModelScope.launch {
            launch {
                post.medias.scaled?.let { media ->
                    scaledImageFileSizeStr = "Loading..."

                    networkRepository.getFileSize(media.url).collect { result ->
                        scaledImageFileSizeStr = when (result) {
                            is Result.Success -> convertFileSizeUseCase(result.data)
                            else -> "Error!"
                        }
                    }
                }
            }

            launch {
                originalImageFileSizeStr = "Loading..."

                networkRepository.getFileSize(post.medias.original.url).collect { result ->
                    originalImageFileSizeStr = when (result) {
                        is Result.Success -> convertFileSizeUseCase(result.data)
                        else -> "Error!"
                    }
                }
            }
        }
    }

    fun launchUrl(context: Context, url: String) {
        val parsed = when {
            url.isAHttpUrl() -> url
            else -> "https://www.google.com/search?q=$url"
        }

        customTabsIntentBuilder
            .build()
            .launchUrl(context, parsed.toUri())
    }

    private fun String.isAHttpUrl(): Boolean {
        val http = "http://"
        val https = "https://"

        return when {
            length <= https.length -> false
            substring(startIndex = 0, endIndex = https.length) == https -> true
            substring(startIndex = 0, endIndex = http.length) == http -> true
            else -> false
        }
    }

    private fun getUploaderName(userId: Int) {
        viewModelScope.launch {
            Timber.d("getting uploader name...")

            when (val result = getMemberUseCase.invoke(userId)) {
                is Result.Success -> Timber.d("uploader name = ${result.data.name}")
                is Result.Failed -> Timber.d("Error: ${result.message}")
                is Result.Error -> Timber.d("Error: ${result.t}")
            }
        }
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(text, text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Toast
                .makeText(context, "Source copied to clipboard!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun generatePostLink(): String {
        return danbooruRepository.host.parsePostLink(postId)
    }
}
