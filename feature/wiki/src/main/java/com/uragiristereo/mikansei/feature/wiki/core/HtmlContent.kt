package com.uragiristereo.mikansei.feature.wiki.core

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.getCategoryColor
import com.uragiristereo.mikansei.feature.wiki.core.HtmlContentState.ClickEvent

@Stable
class HtmlContentState(
    val webView: WebView,
    private val onClickEvent: (ClickEvent) -> Unit,
) {
    var isContentRendered by mutableStateOf(false); private set
    var colors by mutableStateOf<Colors?>(null)

    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            isContentRendered = false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            isContentRendered = true
        }

        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest,
        ): Boolean {
            processLinks(request.url)

            return true
        }
    }

    init {
        webView.webViewClient = webViewClient
    }

    fun loadContent(html: String, theme: Colors) {
        webView.loadDataWithBaseURL(
            /* baseUrl = */ DUMMY_BASE_URL,
            /* data = */ generateCss(theme) + html,
            /* mimeType = */ "text/html",
            /* encoding = */ "utf-8",
            /* historyUrl = */ null,
        )
    }

    private fun processLinks(url: Uri) {
        val urlString = url.toString()
        val withoutBaseUrl = urlString.replace(DUMMY_BASE_URL, "")

        when {
            withoutBaseUrl.startsWith("/wiki_pages/") -> {
                val tag = withoutBaseUrl.replace("/wiki_pages/", "")
                onClickEvent(ClickEvent.WikiItem(tag))
            }

            withoutBaseUrl.startsWith("/posts/") -> {
                val postId = withoutBaseUrl.replace("/posts/", "").toInt()
                onClickEvent(ClickEvent.Post(postId))
            }

            withoutBaseUrl.startsWith("/posts?tags=") -> {
                val tags = withoutBaseUrl.replace("/posts?tags=", "")
                onClickEvent(ClickEvent.Browse(tags))
            }

            urlString.startsWith(DUMMY_BASE_URL) -> {
                onClickEvent(ClickEvent.DanbooruUrl(withoutBaseUrl))
            }

            urlString.startsWith("https://") || urlString.startsWith("http://") -> {
                onClickEvent(ClickEvent.ExternalUrl(url))
            }
        }
    }

    private fun generateCss(theme: Colors): String {
        return """
            <style>
                body {
                    background-color: ${theme.background.toCssColor()};
                    color: ${theme.onBackground.toCssColor()};
                }
                ::selection {
                    color: ${theme.background.toCssColor()};
                    background: ${theme.primary.toCssColor()};
                }
                a:link {
                    color: ${theme.primary.toCssColor()};
                }
                a:hover {
                    color: ${theme.primary.toCssColor()};
                }
                a.dtext-wiki-link {
                    color: ${Tag.Category.GENERAL.toCssColor(theme)};
                }
                a.tag-type-0 {
                    color: ${Tag.Category.GENERAL.toCssColor(theme)};
                }
                a.tag-type-1 {
                    color: ${Tag.Category.ARTIST.toCssColor(theme)};
                }
                a.tag-type-2 {
                    color: ${Tag.Category.UNKNOWN.toCssColor(theme)};
                }
                a.tag-type-3 {
                    color: ${Tag.Category.COPYRIGHT.toCssColor(theme)};
                }
                a.tag-type-4 {
                    color: ${Tag.Category.CHARACTER.toCssColor(theme)};
                }
                a.tag-type-5 {
                    color: ${Tag.Category.META.toCssColor(theme)};
                }
                div.spoiler {
                    background-color: ${theme.onBackground.toCssColor()};
                    color: ${theme.onBackground.toCssColor()};
                }
                div.spoiler:hover {
                    background-color: ${theme.onBackground.toCssColor()};
                    color: ${theme.background.toCssColor()};
                }
            </style>
        """.trimIndent()
    }

    private fun Color.toCssColor(): String {
        val argb = toArgb()
        val red = argb.red
        val green = argb.green
        val blue = argb.blue

        return "rgba($red, $green, $blue, $alpha)"
    }

    private fun Tag.Category.toCssColor(theme: Colors): String {
        return getCategoryColor(theme.isLight).toCssColor()
    }

    companion object {
        private const val DUMMY_BASE_URL = "https://danbooru.donmai.us"
    }

    sealed interface ClickEvent {
        data class WikiItem(val tag: String) : ClickEvent
        data class Post(val postId: Int) : ClickEvent
        data class Browse(val tags: String) : ClickEvent
        data class DanbooruUrl(val path: String) : ClickEvent
        data class ExternalUrl(val url: Uri) : ClickEvent
    }
}

@Composable
fun rememberHtmlContentState(onClickEvent: (ClickEvent) -> Unit): HtmlContentState {
    val context = LocalContext.current
    val onClickEventState by rememberUpdatedState(onClickEvent)

    return remember {
        HtmlContentState(
            webView = WebView(context),
            onClickEvent = onClickEventState,
        )
    }
}

@Composable
fun HtmlContent(
    state: HtmlContentState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = state.isContentRendered,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxWidth(),
    ) {
        AndroidView(
            factory = { state.webView },
            modifier = Modifier,
        )
    }
}
