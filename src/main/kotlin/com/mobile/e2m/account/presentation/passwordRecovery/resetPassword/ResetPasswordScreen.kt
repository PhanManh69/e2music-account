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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.mobile.e2m.account.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.composable.E2MHeader
import com.mobile.e2m.core.ui.composable.E2MScaffold
import com.mobile.e2m.core.ui.composable.inputField.E2MTextField
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun ResetPasswordScreen(
    goBack: () -> Unit = { },
    goToLogin: () -> Unit = { },
) {
    ResetPasswordScaffold(
        goBack = { goBack() },
        loginOnClick = { goToLogin() },
    )
}

@Composable
private fun ResetPasswordScaffold(
    modifier: Modifier = Modifier,
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
                    title = getString().resetPasswordTxt,
                    iconId = R.drawable.ic_angle_left,
                    leadingIconOnClick = { goBack() }
                )
            },
            content = {
                ResetPasswordContent(
                    loginOnClick = { loginOnClick() }
                )
            }
        )
    }
}

@Composable
private fun ResetPasswordContent(
    modifier: Modifier = Modifier,
    loginOnClick: () -> Unit = { },
) {
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
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
                value = newPassword.value,
                onValueChange = { newPassword.value = it },
                placeholder = getString().enterNewPasswordTxt,
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
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                placeholder = getString().enterConfirmPasswordTxt,
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
