package com.mobile.e2m.account.presentation.login.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun LoginOtherMethodButton(
    modifier: Modifier = Modifier,
    loginGoogleOnClick: () -> Unit = { },
    loginFacebookOnClick: () -> Unit = { },
    loginAppleIdOnClick: () -> Unit = { },
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(E2MTheme.alias.size.spacing.large)
    ) {
        E2MButton(
            modifier = Modifier.fillMaxWidth(),
            title = getString().loginWithGoogleTxt,
            iconId = R.raw.ic_logo_google,
            onClick = { loginGoogleOnClick() }
        )

        E2MButton(
            modifier = Modifier.fillMaxWidth(),
            title = getString().loginWithFacebookTxt,
            iconId = R.raw.ic_logo_facebook,
            onClick = { loginFacebookOnClick() }
        )

        E2MButton(
            modifier = Modifier.fillMaxWidth(),
            title = getString().loginWithAppleIDTxt,
            iconId = R.raw.ic_logo_apple,
            onClick = { loginAppleIdOnClick() }
        )
    }
}
