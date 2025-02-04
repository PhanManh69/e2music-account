package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.mobile.e2m.account.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.composable.E2MHeader
import com.mobile.e2m.core.ui.composable.E2MIdentityPasscode
import com.mobile.e2m.core.ui.composable.E2MScaffold
import com.mobile.e2m.core.ui.composable.inputField.E2MTextField
import com.mobile.e2m.core.ui.theme.E2MTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ForgotPasswordScreen(
    goBack: () -> Unit = { },
    goToResetPassword: () -> Unit = { },
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    ForgotPasswordScaffold(
        goBack = { goBack() },
        confirmOnClick = { goToResetPassword() },
        sendOtpOnClick = {
            if (state.countdown == 0) {
                viewModel.trySendAction(ForgotPasswordAction.SendOtpClick)
            }
        },
        sendOtpText = if (state.countdown > 0) {
            "${context.getString(com.mobile.e2m.core.ui.R.string.resendCodeLater)} (${state.countdown}s)"
        } else {
            context.getString(state.sendOtpTextResId)
        }
    )
}

@Composable
private fun ForgotPasswordScaffold(
    modifier: Modifier = Modifier,
    sendOtpText: String? = null,
    goBack: () -> Unit = { },
    sendOtpOnClick: () -> Unit = { },
    confirmOnClick: () -> Unit = { },
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { focusManager.clearFocus() }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.raw.img_background_dark)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        E2MScaffold(
            topBar = {
                E2MHeader(
                    title = getString().forgotPasswordTxt,
                    iconId = R.drawable.ic_angle_left,
                    leadingIconOnClick = { goBack() }
                )
            },
            content = {
                ForgotPasswordContent(
                    sendOtpText = sendOtpText,
                    sendOtpOnClick = { sendOtpOnClick() },
                    confirmOnClick = { confirmOnClick() },
                )
            }
        )
    }
}

@Composable
private fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    sendOtpText: String? = null,
    sendOtpOnClick: () -> Unit = { },
    confirmOnClick: () -> Unit = { },
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val enterAccountEmail = remember { mutableStateOf("") }
    val style = E2MTheme.typography
    val color = E2MTheme.alias.color
    val size = E2MTheme.alias.size

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = size.spacing.small)
            .padding(bottom = size.spacing.small)
    ) {
        Column(
            modifier = Modifier.padding(top = size.spacing.large5x),
            verticalArrangement = Arrangement.spacedBy(size.spacing.largeX),
        ) {
            E2MTextField(
                value = enterAccountEmail.value,
                onValueChange = { enterAccountEmail.value = it },
                placeholder = getString().enterAccountEmailTxt,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            E2MIdentityPasscode(
                doneOnClick = { confirmOnClick() }
            )

            sendOtpText?.let {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { sendOtpOnClick() },
                    text = it,
                    style = style.base.bold,
                    color = color.text.white,
                    textAlign = TextAlign.Center,
                )
            }
        }

        E2MButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomPadding)
                .align(Alignment.BottomCenter),
            title = getString().confirmTxt,
            style = Gradient,
            onClick = { confirmOnClick() }
        )
    }
}
