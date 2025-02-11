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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.mobile.e2m.account.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.composable.E2MHeader
import com.mobile.e2m.core.ui.composable.E2MScaffold
import com.mobile.e2m.core.ui.composable.background.E2MBackgroundDark
import com.mobile.e2m.core.ui.composable.debounceClickable
import com.mobile.e2m.core.ui.composable.inputField.E2MIdentityPasscode
import com.mobile.e2m.core.ui.composable.inputField.E2MTextField
import com.mobile.e2m.core.ui.theme.E2MTheme
import com.mobile.e2m.core.ui.util.EventsEffect
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ForgotPasswordScreen(
    goBack: () -> Unit = { },
    goToResetPassword: (Int) -> Unit = { },
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    EventsEffect(viewModel) { event ->
        when (event) {
            is ForgotPasswordEvent.GoToResetPasswordScreen -> goToResetPassword(event.userId)
        }
    }

    ForgotPasswordScaffold(
        emailAccount = state.emailAccount,
        passcode = state.passcode,
        emailAccountError = state.emailAccountError?.let { context.getString(it) },
        passcodeError = state.passcodeError?.let { context.getString(it) },
        sendOtpText = if (state.countdown > 0) {
            "${context.getString(com.mobile.e2m.core.ui.R.string.resendCodeLater)} (${state.countdown}s)"
        } else {
            context.getString(state.sendOtpTextResId)
        },
        onEmailTyped = { value, _ ->
            viewModel.trySendAction(ForgotPasswordAction.OnEmailTyped(value))
        },
        onPasscodeTyped = { value, _ ->
            viewModel.trySendAction(ForgotPasswordAction.OnPasscodeTyped(value))
        },
        goBack = { goBack() },
        confirmOnClick = {
            viewModel.trySendAction(ForgotPasswordAction.ConfirmClick)
        },
        sendOtpOnClick = {
            if (state.countdown == 0) {
                viewModel.trySendAction(ForgotPasswordAction.SendOtpClick)
            }
        },
    )
}

@Composable
private fun ForgotPasswordScaffold(
    modifier: Modifier = Modifier,
    emailAccount: String = "",
    passcode: String = "",
    emailAccountError: String? = null,
    passcodeError: String? = null,
    sendOtpText: String? = null,
    onEmailTyped: (String, String?) -> Unit = { _, _ -> },
    onPasscodeTyped: (String, String?) -> Unit = { _, _ -> },
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
        E2MBackgroundDark()

        E2MScaffold(
            topBar = {
                E2MHeader(
                    title = getString().forgotPasswordTxt,
                    leadingIconId = R.drawable.ic_angle_left,
                    leadingIconOnClick = { goBack() }
                )
            },
            content = {
                ForgotPasswordContent(
                    emailAccount = emailAccount,
                    passcode = passcode,
                    emailAccountError = emailAccountError,
                    passcodeError = passcodeError,
                    sendOtpText = sendOtpText,
                    onEmailTyped = onEmailTyped,
                    onPasscodeTyped = onPasscodeTyped,
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
    emailAccount: String = "",
    passcode: String = "",
    emailAccountError: String? = null,
    passcodeError: String? = null,
    sendOtpText: String? = null,
    onEmailTyped: (String, String?) -> Unit = { _, _ -> },
    onPasscodeTyped: (String, String?) -> Unit = { _, _ ->},
    sendOtpOnClick: () -> Unit = { },
    confirmOnClick: () -> Unit = { },
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
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
                initText = emailAccount,
                onValueChange = { onEmailTyped(it, emailAccountError) },
                placeholder = getString().enterAccountEmailTxt,
                caption = emailAccountError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            E2MIdentityPasscode(
                initPasscode = passcode,
                onPasscodeChange = { onPasscodeTyped(it, passcodeError) },
                caption = passcodeError,
                doneOnClick = { confirmOnClick() }
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                sendOtpText?.let {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .debounceClickable { sendOtpOnClick() },
                        text = it,
                        style = style.base.bold,
                        color = color.text.white,
                        textAlign = TextAlign.Center,
                    )
                }
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
