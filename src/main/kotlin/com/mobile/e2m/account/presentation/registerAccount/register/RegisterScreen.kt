package com.mobile.e2m.account.presentation.registerAccount.register

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
import com.mobile.e2m.account.presentation.registerAccount.register.composable.RegisterAuthenticateOtp
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
internal fun RegisterScreen(
    goBack: () -> Unit = { },
    goToRegistrationSuccess: () -> Unit = { },
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }

    EventsEffect(viewModel) { event ->
        when (event) {
            is RegisterEvent.GoToRegistrationSuccess -> {
                openDialog.value = state.openDialog
                goToRegistrationSuccess()
            }

            is RegisterEvent.OpenOtpDialog -> {
                openDialog.value = event.openDialog
            }
        }
    }

    RegisterScaffold(
        username = state.username,
        fullname = state.fullname,
        email = state.email,
        newPassword = state.newPassword,
        confirmPassword = state.confirmPassword,
        usernameError = state.usernameError?.let { context.getString(it) },
        fullnameError = state.fullnameError?.let { context.getString(it) },
        emailError = state.emailError?.let { context.getString(it) },
        newPasswordError = state.newPasswordError?.let { context.getString(it) },
        confirmPasswordError = state.confirmPasswordError?.let { context.getString(it) },
        onUsernameTyped = { value, _ ->
            viewModel.trySendAction(RegisterAction.OnUsernameTyped(value))
        },
        onFullnameTyped = { value, _ ->
            viewModel.trySendAction(RegisterAction.OnFullnameTyped(value))
        },
        onEmailTyped = { value, _ ->
            viewModel.trySendAction(RegisterAction.OnEmailTyped(value))
        },
        onNewPasswordTyped = { value, _ ->
            viewModel.trySendAction(RegisterAction.OnNewPasswordTyped(value))
        },
        onConfirmPasswordTyped = { value, _ ->
            viewModel.trySendAction(RegisterAction.OnConfirmPasswordTyped(value))
        },
        goBack = { goBack() },
        registerOnClick = {
            viewModel.trySendAction(RegisterAction.RegisterClick(openDialog = true))
        },
    )

    RegisterAuthenticateOtp(
        email = state.email,
        passcode = state.passcode,
        passcodeError = state.passcodeError?.let { context.getString(it) },
        openDialog = openDialog,
        sendOtpText = if (state.countdown > 0) {
            "${context.getString(com.mobile.e2m.core.ui.R.string.resendCodeLater)} (${state.countdown}s)"
        } else {
            context.getString(state.sendOtpTextResId)
        },
        onPasscodeTyped = { value, _ ->
            viewModel.trySendAction(RegisterAction.OnPasscodeTyped(value))
        },
        sendOtpOnClick = {
            if (state.countdown == 0) {
                viewModel.trySendAction(RegisterAction.SendOtpClick)
            }
        },
        confirmOnClick = {
            viewModel.trySendAction(RegisterAction.ConfirmClick(openDialog = state.openDialog))
        },
    )
}

@Composable
private fun RegisterScaffold(
    modifier: Modifier = Modifier,
    username: String = "",
    fullname: String = "",
    email: String = "",
    newPassword: String = "",
    confirmPassword: String = "",
    usernameError: String? = null,
    fullnameError: String? = null,
    emailError: String? = null,
    newPasswordError: String? = null,
    confirmPasswordError: String? = null,
    onUsernameTyped: (String, String?) -> Unit = { _, _ -> },
    onFullnameTyped: (String, String?) -> Unit = { _, _ -> },
    onEmailTyped: (String, String?) -> Unit = { _, _ -> },
    onNewPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    onConfirmPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    goBack: () -> Unit = { },
    registerOnClick: () -> Unit = { },
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
                    title = getString().registerTxt,
                    iconId = R.drawable.ic_angle_left,
                    leadingIconOnClick = { goBack() }
                )
            },
            content = {
                RegisterContent(
                    username = username,
                    fullname = fullname,
                    email = email,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword,
                    usernameError = usernameError,
                    fullnameError = fullnameError,
                    emailError = emailError,
                    newPasswordError = newPasswordError,
                    confirmPasswordError = confirmPasswordError,
                    onUsernameTyped = onUsernameTyped,
                    onFullnameTyped = onFullnameTyped,
                    onEmailTyped = onEmailTyped,
                    onNewPasswordTyped = onNewPasswordTyped,
                    onConfirmPasswordTyped = onConfirmPasswordTyped,
                    registerOnClick = registerOnClick,
                )
            }
        )
    }
}

@Composable
private fun RegisterContent(
    modifier: Modifier = Modifier,
    username: String = "",
    fullname: String = "",
    email: String = "",
    newPassword: String = "",
    confirmPassword: String = "",
    usernameError: String? = null,
    fullnameError: String? = null,
    emailError: String? = null,
    newPasswordError: String? = null,
    confirmPasswordError: String? = null,
    onUsernameTyped: (String, String?) -> Unit = { _, _ -> },
    onFullnameTyped: (String, String?) -> Unit = { _, _ -> },
    onEmailTyped: (String, String?) -> Unit = { _, _ -> },
    onNewPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    onConfirmPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    registerOnClick: () -> Unit = { },
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
                initText = username,
                onValueChange = { onUsernameTyped(it, usernameError) },
                placeholder = getString().enterUsernameTxt,
                caption = usernameError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            E2MTextField(
                initText = fullname,
                onValueChange = { onFullnameTyped(it, fullnameError) },
                placeholder = getString().enterFullnameTxt,
                caption = fullnameError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            E2MTextField(
                initText = email,
                onValueChange = { onEmailTyped(it, emailError) },
                placeholder = getString().enterEmailTxt,
                caption = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

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
                    onDone = { registerOnClick() }
                )
            )
        }

        E2MButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomPadding)
                .align(Alignment.BottomCenter),
            title = getString().registerTxt,
            style = Gradient,
            onClick = { registerOnClick() }
        )
    }
}
