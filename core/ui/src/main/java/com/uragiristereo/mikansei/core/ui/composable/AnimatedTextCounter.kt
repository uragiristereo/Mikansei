package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun AnimatedTextCounter(
    count: Int?,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    val countFallbackZero = count ?: 0

    if (count != null) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.animateContentSize(),
        ) {
            countFallbackZero
                .toString()
                .mapIndexed { index, char ->
                    Digit(
                        digitChar = char,
                        fullNumber = countFallbackZero,
                        place = index,
                    )
                }
                .forEach { digit ->
                    AnimatedContent(
                        targetState = digit,
                        transitionSpec = {
                            if (targetState != initialState) {
                                slideInVertically {
                                    if (targetState > initialState) -it else it
                                } togetherWith slideOutVertically {
                                    if (targetState > initialState) it else -it
                                } using SizeTransform(clip = false)
                            } else {
                                EnterTransition.None togetherWith ExitTransition.None
                            }
                        },
                    ) { d ->
                        Text(
                            text = "${d.digitChar}",
                            color = color,
                            fontSize = fontSize,
                            fontStyle = fontStyle,
                            fontWeight = fontWeight,
                            style = style,
                        )
                    }
                }
        }
    } else {
        Text(
            text = "0",
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            style = style,
            modifier = modifier,
        )
    }
}

private data class Digit(
    val digitChar: Char,
    val fullNumber: Int,
    val place: Int,
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Digit -> digitChar == other.digitChar
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

private operator fun Digit.compareTo(other: Digit): Int {
    return fullNumber.compareTo(other.fullNumber)
}
