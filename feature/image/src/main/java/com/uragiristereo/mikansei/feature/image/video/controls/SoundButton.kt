package com.uragiristereo.mikansei.feature.image.video.controls

import android.widget.Toast
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun SoundButton(
    noSound: Boolean,
    muted: Boolean,
    onToggleMuted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    if (noSound) {
        IconButton(
            onClick = {
                Toast.makeText(context, "Video has no sound", Toast.LENGTH_SHORT).show()
            },
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.no_sound),
                contentDescription = null,
                tint = Color.White,
            )
        }
    } else {
        IconButton(
            onClick = onToggleMuted,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(
                    id = when {
                        muted -> R.drawable.volume_off
                        else -> R.drawable.volume_up
                    },
                ),
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}
