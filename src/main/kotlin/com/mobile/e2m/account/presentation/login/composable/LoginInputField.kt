package com.mobile.e2m.account.presentation.login.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.inputField.E2MTextField
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun LoginInputField(
    emailAccount: String = "",
    password: String = "",
    emailAccountError: String? = null,
    passwordError: String? = null,
    onFocusChanged: (Boolean) -> Unit,
    onEmailAccTyped: (String, String?) -> Unit = { _, _ -> },
    onPasswordTyped: (String, String?) -> Unit = { _, _ -> },
    goToHome: () -> Unit = { },
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        E2MTextField(
            initText = emailAccount,
            onValueChange = { onEmailAccTyped(it, emailAccountError) },
            placeholder = getString().enterAccountEmailTxt,
            caption = emailAccountError,
            onFocusChanged = {
                onFocusChanged.invoke(it.hasFocus)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(E2MTheme.alias.size.spacing.large))

        E2MTextField(
            initText = password,
            onValueChange = { onPasswordTyped(it, passwordError) },
            placeholder = getString().enterPasswordTxt,
            caption = passwordError,
            iconId = if (isPasswordVisible) R.drawable.ic_hide_password else R.drawable.ic_display_password,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIconOnClick = {
                isPasswordVisible = !isPasswordVisible
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPasswordVisible) KeyboardType.Text else KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { goToHome() }
            )
        )
    }
}
