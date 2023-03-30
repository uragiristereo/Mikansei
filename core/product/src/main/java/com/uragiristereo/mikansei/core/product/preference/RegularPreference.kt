package com.uragiristereo.mikansei.core.product.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.resources.R

@Composable
fun RegularPreference(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
) {
    RegularPreference(
        title = title,
        subtitle = AnnotatedString(text = subtitle),
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        enabled = enabled,
    )
}

@Composable
fun RegularPreference(
    title: String,
    subtitle: AnnotatedString?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    enabled: Boolean = true,
) {
    val iconPadding = LocalIconPadding.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick,
            )
            .padding(all = 16.dp),
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 32.dp),
            )
        }

        if (icon == null && iconPadding) {
            Spacer(
                modifier = Modifier.padding(end = 56.dp),
            )
        }

        PreferenceContainer(
            title = title,
            subtitle = subtitle,
            enabled = enabled,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegularPreferencePreview() {
    RegularPreference(
        title = "Advanced settings",
        subtitle = AnnotatedString(text = "Lorem ipsum dolor sit amet"),
        icon = painterResource(id = R.drawable.history),
        onClick = { },
    )
}
