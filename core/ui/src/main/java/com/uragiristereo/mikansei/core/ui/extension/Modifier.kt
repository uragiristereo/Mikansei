package com.uragiristereo.mikansei.core.ui.extension

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.alphabet(
    alphabet: Boolean,
    threshold: Int = 10,
    operation: () -> Unit,
): Modifier {
    return this.composed {
        var count by remember { mutableIntStateOf(0) }

        pointerInput(key1 = Unit) {
            detectTapGestures {
                if (alphabet) {
                    count++

                    if (count >= threshold) {
                        count = 0
                        operation()
                    }
                }
            }
        }
    }
}

fun Modifier.thenIf(
    predicate: Boolean,
    ifTrue: Modifier.() -> Modifier,
): Modifier {
    return when {
        predicate -> then(ifTrue(Modifier))
        else -> this
    }
}

fun Modifier.thenIf(
    predicate: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier,
): Modifier {
    return then(
        when {
            predicate -> ifTrue(Modifier)
            else -> ifFalse(Modifier)
        }
    )
}

fun <T : Any> Modifier.thenIfNotNull(
    element: T?,
    ifTrue: Modifier.(T) -> Modifier,
): Modifier {
    return when {
        element != null -> then(ifTrue(element))
        else -> this
    }
}

fun <T> Modifier.thenIfNotNull(
    element: T?,
    ifTrue: Modifier.(T) -> Modifier,
    ifFalse: Modifier.() -> Modifier,
): Modifier {
    return then(
        when {
            element != null -> ifTrue(element)
            else -> ifFalse(Modifier)
        }
    )
}
