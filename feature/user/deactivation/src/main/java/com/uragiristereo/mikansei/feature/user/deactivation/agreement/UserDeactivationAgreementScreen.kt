package com.uragiristereo.mikansei.feature.user.deactivation.agreement

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uragiristereo.mikansei.core.product.theme.MikanseiTheme
import com.uragiristereo.mikansei.core.product.theme.Theme
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import kotlinx.coroutines.launch

@Composable
fun UserDeactivationAgreementScreen(
    innerPadding: PaddingValues,
    activeUserId: Int,
    isConfirming: Boolean,
    onConfirmingChange: (Boolean) -> Unit,
    onProceedClick: () -> Unit,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val checkBoxAgreementEnabled = !scrollState.canScrollForward || isConfirming

    val scrollValue = remember {
        density.run { 100.dp.toPx() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(innerPadding),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(
                            "You can deactivate your account here in the app or in the Danbooru site. " +
                                    "Deactivating your account will delete your private account information, " +
                                    "but it will not delete your contributions to the site.\n\n" +
                                    "Deactivating your account will do the following things:\n\n" +
                                    "•\t\tChange your username to a generic username (user_$activeUserId).\n" +
                                    "•\t\tDelete your password, email address, API keys, and account settings.\n" +
                                    "•\t\tDelete your saved searches.\n" +
                                    "•\t\tDelete your private favorite groups.\n" +
                                    "•\t\tDelete your private favorites and upvotes (only if privacy mode is enabled).\n\n",
                        )
                        append("The following things will ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("not")
                        }
                        append(" be deleted:\n\n")
                        append(
                            "•\t\tPosts you've uploaded.\n" +
                                    "•\t\tYour comments, forum posts, and private messages.\n" +
                                    "•\t\tYour tag edits, wiki edits, translation notes, and any other contributions you've made to the site.\n" +
                                    "•\t\tYour login history, including your IP address and geographic location. This is kept for moderation purposes."
                        )
                    },
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                )
            }

            if (!checkBoxAgreementEnabled) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(percent = 50),
                        )
                        .clip(RoundedCornerShape(percent = 50))
                        .background(
                            color = MaterialTheme.colors.background
                                .backgroundElevation(4.dp)
                                .copy(alpha = 0.8f),
                        )
                        .clickable {
                            scope.launch {
                                scrollState.animateScrollBy(scrollValue)
                            }
                        }
                        .padding(all = 8.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.expand_more),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Scroll more".uppercase(),
                        style = MaterialTheme.typography.overline,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                    )
                }
            }
        }

        Divider()

        val interactionSource = remember { MutableInteractionSource() }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = checkBoxAgreementEnabled,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = {
                        onConfirmingChange(!isConfirming)
                    },
                )
                .padding(vertical = 8.dp)
                .padding(end = 16.dp),
        ) {
            Checkbox(
                enabled = checkBoxAgreementEnabled,
                interactionSource = interactionSource,
                checked = isConfirming,
                onCheckedChange = onConfirmingChange,
                modifier = Modifier.padding(start = 2.dp),
            )

            Spacer(Modifier.width(2.dp))

            Text(
                text = "I acknowledged that I want to deactivate my Danbooru account",
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = when {
                        checkBoxAgreementEnabled -> ContentAlpha.high
                        else -> ContentAlpha.disabled
                    },
                ),
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            enabled = isConfirming,
            onClick = onProceedClick,
            content = {
                Text("Next".uppercase())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
        )
    }
}

@Preview
@Preview(name = "Short", heightDp = 400)
@Composable
private fun UserDeactivationAgreementScreenPreview() {
    var isConfirming by remember { mutableStateOf(false) }

    MikanseiTheme(theme = Theme.DARK) {
        Surface {
            UserDeactivationAgreementScreen(
                innerPadding = PaddingValues(0.dp),
                activeUserId = 123456,
                isConfirming = isConfirming,
                onConfirmingChange = { isConfirming = it },
                onProceedClick = {},
            )
        }
    }
}
