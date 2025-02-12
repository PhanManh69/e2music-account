package com.mobile.e2m.account.presentation.started

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.composable.E2MAsyncImage
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun StartedScreen(
    goToLogin: () -> Unit = { },
) {
    var isClickedLogin = true

    StartedScaffold(
        loginOnClick = {
            if (isClickedLogin) {
                isClickedLogin = false
                goToLogin()
            }
        }
    )
}

@Composable
private fun StartedScaffold(
    modifier: Modifier = Modifier,
    startOnClick: () -> Unit = { },
    loginOnClick: () -> Unit = { },
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        E2MAsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageId = R.raw.img_background_started,
            contentScale = ContentScale.Crop,
        )

        StartedContent(
            modifier = Modifier.align(Alignment.BottomCenter),
            startOnClick = { startOnClick() },
            loginOnClick = { loginOnClick() },
        )
    }
}

@Composable
private fun StartedContent(
    modifier: Modifier = Modifier,
    startOnClick: () -> Unit = { },
    loginOnClick: () -> Unit = { },
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val style = E2MTheme.typography
    val color = E2MTheme.alias.color
    val size = E2MTheme.alias.size

    Column(
        modifier = modifier.padding(size.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString().titleStartedTxt,
            style = style.h1.black,
            color = color.text.white,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(size.spacing.small))

        Text(
            text = getString().introduceStartedTxt,
            style = style.small.regular,
            color = color.text.white,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(size.spacing.large5x))

        E2MButton(
            modifier = Modifier.fillMaxWidth(),
            title = getString().startTxt,
            style = Gradient,
            onClick = { startOnClick() },
        )

        Spacer(modifier = Modifier.height(size.spacing.large))

        E2MButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomPadding),
            title = getString().loginTxt,
            onClick = { loginOnClick() },
        )
    }
}
