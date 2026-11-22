package com.uragiristereo.mikansei.feature.image.image.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import me.saket.telephoto.zoomable.DoubleClickToZoomListener
import me.saket.telephoto.zoomable.DynamicZoomSpec
import me.saket.telephoto.zoomable.ZoomSpec

private const val MAX_ZOOM = 5f
private const val DOUBLE_TAP_ZOOM = 2f

@Composable
fun rememberImageZoomSpec(): DynamicZoomSpec {
    return remember {
        DynamicZoomSpec { inputs ->
            val unscaled = inputs.unscaledContentSize.maxDimension
            val fitScale = when {
                unscaled > 0f -> inputs.scaledContentBounds.size.maxDimension / unscaled
                else -> 1f
            }

            ZoomSpec(maxZoomFactor = MAX_ZOOM * fitScale)
        }
    }
}

/**
 * Double tap zooms to [DOUBLE_TAP_ZOOM] times the fit scale, and tapping again while zoomed at all
 * resets, as TouchImageView did.
 *
 * [DoubleClickToZoomListener.cycle] can't express this: its `maxZoomFactor` is measured against the
 * image's original pixel size, so it never recognises that it is already zoomed in and double tap
 * becomes one-way. [imageZoomSpec] makes `maximum.factor` equal to `MAX_ZOOM * fitScale`, so
 * dividing it back out recovers the fit scale without threading it through separately.
 */
@Composable
fun rememberImageDoubleClickListener(): DoubleClickToZoomListener {
    return remember {
        DoubleClickToZoomListener { state, centroid ->
            val isZoomedIn = (state.zoomFraction ?: 0f) > 0.01f

            when {
                isZoomedIn -> state.resetZoom()

                else -> state.zoomTo(
                    zoomFactor = state.zoomSpec.maximum.factor / MAX_ZOOM * DOUBLE_TAP_ZOOM,
                    centroid = centroid,
                )
            }
        }
    }
}
