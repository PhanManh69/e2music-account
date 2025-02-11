package com.mobile.e2m.account.presentation.passwordRecovery.resetPassword

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.mobile.e2m.account.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.composable.E2MHeader
import com.mobile.e2m.core.ui.composable.E2MScaffold
import com.mobile.e2m.core.ui.composable.background.E2MBackgroundDark
import com.mobile.e2m.core.ui.composable.inputField.E2MTextField
import com.mobile.e2m.core.ui.theme.E2MTheme
import com.mobile.e2m.core.ui.util.EventsEffect
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ResetPasswordScreen(
    userId: Int? = null,
    goBack: () -> Unit = { },
    goToLogin: () -> Unit = { },
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        userId?.let {
            viewModel.setUserId(it)
        }
    }

    EventsEffect(viewModel) { event ->
        when (event) {
            ResetPasswordEvent.GoToLoginScreen -> goToLogin()
        }
    }

    ResetPasswordScaffold(
        newPassword = state.newPassword,
        confirmPassword = state.confirmPassword,
        newPasswordError = state.newPasswordError?.let { context.getString(it) },
        confirmPasswordError = state.confirmPasswordError?.let { context.getString(it) },
        onNewPasswordTyped = { value, _ ->
            viewModel.trySendAction(ResetPasswordAction.OnNewPasswordTyped(value))
        },
        onConfirmPasswordTyped = { value, _ ->
            viewModel.trySendAction(ResetPasswordAction.OnConfirmPasswordTyped(value))
        },
        goBack = { goBack() },
        loginOnClick = {
            viewModel.trySendAction(ResetPasswordAction.ConfirmClick)
        },
    )
}

@Composable
private fun ResetPasswordScaffold(
    modifier: Modifier = Modifier,
    newPassword: String = "",
    confirmPassword: String = "",
    newPasswordError: String? = null,
    confirmPasswordError: String? = null,
    onNewPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    onConfirmPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    goBack: () -> Unit = { },
    loginOnClick: () -> Unit = { },
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
                    title = getString().resetPasswordTxt,
                    leadingIconId = R.drawable.ic_angle_left,
                    leadingIconOnClick = { goBack() }
                )
            },
            content = {
                ResetPasswordContent(
                    newPassword = newPassword,
                    confirmPassword = confirmPassword,
                    newPasswordError = newPasswordError,
                    confirmPasswordError = confirmPasswordError,
                    onNewPasswordTyped = onNewPasswordTyped,
                    onConfirmPasswordTyped = onConfirmPasswordTyped,
                    loginOnClick = { loginOnClick() }
                )
            }
        )
    }
}

@Composable
private fun ResetPasswordContent(
    modifier: Modifier = Modifier,
    newPassword: String = "",
    confirmPassword: String = "",
    newPasswordError: String? = null,
    confirmPasswordError: String? = null,
    onNewPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    onConfirmPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    loginOnClick: () -> Unit = { },
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val size = E2MTheme.alias.size

    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

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
                initText = newPassword,
                onValueChange = { onNewPasswordTyped(it, newPasswordError) },
                placeholder = getString().enterNewPasswordTxt,
                caption = newPasswordError,
                iconId = if (isNewPasswordVisible) R.drawable.ic_hide_password else R.drawable.ic_display_password,
                visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIconOnClick = {
                    isNewPasswordVisible = !isNewPasswordVisible
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isNewPasswordVisible) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
            )

            E2MTextField(
                initText = confirmPassword,
                onValueChange = { onConfirmPasswordTyped(it, confirmPasswordError) },
                placeholder = getString().enterConfirmPasswordTxt,
                caption = confirmPasswordError,
                iconId = if (isConfirmPasswordVisible) R.drawable.ic_hide_password else R.drawable.ic_display_password,
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIconOnClick = {
                    isConfirmPasswordVisible = !isConfirmPasswordVisible
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isConfirmPasswordVisible) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { loginOnClick() }
                )
            )
        }

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
