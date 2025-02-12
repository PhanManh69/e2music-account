package com.mobile.e2m.account.presentation.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.account.presentation.login.composable.LoginInputField
import com.mobile.e2m.account.presentation.login.composable.LoginNoAccountRegister
import com.mobile.e2m.account.presentation.login.composable.LoginOtherMethodButton
import com.mobile.e2m.account.presentation.login.composable.LoginOtherMethodLayout
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.composable.E2MHeader
import com.mobile.e2m.core.ui.composable.E2MScaffold
import com.mobile.e2m.core.ui.composable.background.E2MBackgroundDark
import com.mobile.e2m.core.ui.composable.debounceClickable
import com.mobile.e2m.core.ui.theme.E2MTheme
import com.mobile.e2m.core.ui.util.EventsEffect
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun LoginScreen(
    goToMain: (Int) -> Unit = { },
    goToForgotPassword: () -> Unit = { },
    goToRegister: () -> Unit = { },
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    EventsEffect(viewModel) { event ->
        when (event) {
            is LoginEvent.GoToMainScreen -> goToMain(event.userId)

            is LoginEvent.FocusInputFieldChange -> {
                Log.e("EManh Debug", "Check Focus: ${event.isFocus}")
                if (!event.isFocus) {
                    viewModel.trySendAction(LoginAction.GetPassword)
                }
            }
        }
    }

    LoginScaffold(
        emailAccount = state.emailAccount,
        password = state.password,
        emailAccountError = state.emailAccountError?.let { context.getString(it) },
        passwordError = state.passwordError?.let { context.getString(it) },
        onFocusChanged = remember(viewModel) {
            {
                viewModel.trySendAction(LoginAction.OnFocusChange(it))
            }
        },
        onEmailAccTyped = { value, _ ->
            viewModel.trySendAction(LoginAction.OnEmailAccountTyped(value))
        },
        onPasswordTyped = { value, _ ->
            viewModel.trySendAction(LoginAction.OnPasswordTyped(value))
        },
        loginOnClick = {
            viewModel.trySendAction(LoginAction.LoginClick)
        },
        forgotPasswordOnClick = {
            goToForgotPassword()
        },
        registerOnLick = {
            goToRegister()
        }
    )
}

@Composable
private fun LoginScaffold(
    modifier: Modifier = Modifier,
    emailAccount: String = "",
    password: String = "",
    emailAccountError: String? = null,
    passwordError: String? = null,
    onFocusChanged: (Boolean) -> Unit,
    onEmailAccTyped: (String, String?) -> Unit = { _, _ -> },
    onPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    loginOnClick: () -> Unit = { },
    loginGoogleOnClick: () -> Unit = { },
    loginFacebookOnClick: () -> Unit = { },
    loginAppleIdOnClick: () -> Unit = { },
    forgotPasswordOnClick: () -> Unit = { },
    registerOnLick: () -> Unit = { },
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
                    title = getString().loginTxt,
                    leadingIconId = R.drawable.ic_home
                )
            },
            content = {
                LoginContent(
                    emailAccount = emailAccount,
                    password = password,
                    emailAccountError = emailAccountError,
                    passwordError = passwordError,
                    onFocusChanged = onFocusChanged,
                    onEmailAccTyped = onEmailAccTyped,
                    onPasswordTyped = onPasswordTyped,
                    loginOnClick = { loginOnClick() },
                    loginGoogleOnClick = { loginGoogleOnClick() },
                    loginFacebookOnClick = { loginFacebookOnClick() },
                    loginAppleIdOnClick = { loginAppleIdOnClick() },
                    forgotPasswordOnClick = { forgotPasswordOnClick() },
                    registerOnClick = { registerOnLick() }
                )
            }
        )
    }
}

@Composable
private fun LoginContent(
    modifier: Modifier = Modifier,
    emailAccount: String = "",
    password: String = "",
    emailAccountError: String? = null,
    passwordError: String? = null,
    onFocusChanged: (Boolean) -> Unit,
    onEmailAccTyped: (String, String?) -> Unit = { _, _ -> },
    onPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    loginOnClick: () -> Unit = { },
    loginGoogleOnClick: () -> Unit = { },
    loginFacebookOnClick: () -> Unit = { },
    loginAppleIdOnClick: () -> Unit = { },
    forgotPasswordOnClick: () -> Unit = { },
    registerOnClick: () -> Unit = { },
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
        Text(
            modifier = Modifier.padding(top = size.spacing.largeX),
            text = getString().welcomeToLoginTxt,
            style = style.small.regular,
            color = color.text.white,
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier
                .padding(top = size.spacing.large5x, bottom = size.spacing.largeX),
//                .verticalScroll(rememberScrollState()),           Fix delay click button
            verticalArrangement = Arrangement.spacedBy(size.spacing.largeX),
        ) {
            LoginInputField(
                emailAccount = emailAccount,
                password = password,
                emailAccountError = emailAccountError,
                passwordError = passwordError,
                onFocusChanged = onFocusChanged,
                onEmailAccTyped = onEmailAccTyped,
                onPasswordTyped = onPasswordTyped,
                goToHome = { loginOnClick() }
            )

            Row {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier.debounceClickable { forgotPasswordOnClick() },
                    text = getString().passwordRecoveryTxt,
                    style = style.base.bold,
                    color = color.text.white,
                )
            }

            E2MButton(
                modifier = Modifier.fillMaxWidth(),
                title = getString().loginTxt,
                style = Gradient,
                onClick = { loginOnClick() }
            )

            LoginOtherMethodLayout()

            LoginOtherMethodButton(
                loginGoogleOnClick = { loginGoogleOnClick() },
                loginFacebookOnClick = { loginFacebookOnClick() },
                loginAppleIdOnClick = { loginAppleIdOnClick() },
            )
        }

        LoginNoAccountRegister(
            modifier = Modifier
                .padding(bottom = bottomPadding)
                .align(Alignment.BottomCenter),
            registerOnClick = { registerOnClick() }
        )
    }
}
