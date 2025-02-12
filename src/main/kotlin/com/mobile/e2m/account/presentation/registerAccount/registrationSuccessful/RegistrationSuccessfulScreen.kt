package com.mobile.e2m.account.presentation.registerAccount.registrationSuccessful

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.E2MAsyncImage
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.composable.E2MHeader
import com.mobile.e2m.core.ui.composable.E2MScaffold
import com.mobile.e2m.core.ui.composable.background.E2MBackgroundDark
import com.mobile.e2m.core.ui.composable.shadowCustom
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun RegistrationSuccessfulScreen(
    goToLogin: () -> Unit = { },
) {
    RegistrationSuccessfulScaffold(
        loginOnClick = { goToLogin() }
    )
}

@Composable
private fun RegistrationSuccessfulScaffold(
    modifier: Modifier = Modifier,
    loginOnClick: () -> Unit = { },
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        E2MBackgroundDark()

        E2MScaffold(
            topBar = {
                E2MHeader(
                    title = getString().registrationSuccessfulTxt,
                )
            },
            content = {
                RegistrationSuccessfulContent(
                    loginOnClick = { loginOnClick() }
                )
            }
        )
    }
}

@Composable
fun RegistrationSuccessfulContent(
    modifier: Modifier = Modifier,
    loginOnClick: () -> Unit = { },
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val size = E2MTheme.alias.size
    val color = E2MTheme.alias.color
    val style = E2MTheme.typography

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = size.spacing.small)
            .padding(bottom = size.spacing.small)
    ) {
        Text(
            modifier = Modifier
                .padding(top = size.spacing.largeX)
                .align(Alignment.TopCenter),
            text = getString().contentRegistrationSuccessfulTxt,
            style = style.small.regular,
            color = color.text.white,
            textAlign = TextAlign.Center,
        )

        E2MAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .shadowCustom(
                    color = color.surface.shadowDark,
                    shapeRadius = size.spacing.large2X,
                    blurRadius = size.spacing.small4x
                )
                .border(
                    width = size.stroke.thick,
                    color = color.border.textField,
                    shape = RoundedCornerShape(size.spacing.large2X)
                )
                .clip(RoundedCornerShape(size.spacing.large2X))
                .align(Alignment.Center),
            imageId = R.raw.gif_success,
        )

        E2MButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomPadding)
                .align(Alignment.BottomCenter),
            title = getString().loginTxt,
            style = Gradient,
            onClick = { loginOnClick() }
        )
    }
}
