package com.mobile.e2m.account.presentation.registerAccount.register.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.composable.E2MButton
import com.mobile.e2m.core.ui.composable.E2MButtonStyle.Gradient
import com.mobile.e2m.core.ui.composable.E2MDialog
import com.mobile.e2m.core.ui.composable.debounceClickable
import com.mobile.e2m.core.ui.composable.inputField.E2MIdentityPasscode
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun RegisterAuthenticateOtp(
    modifier: Modifier = Modifier,
    email: String = "",
    passcode: String = "",
    passcodeError: String? = null,
    sendOtpText: String? = null,
    openDialog: MutableState<Boolean> = mutableStateOf(false),
    onPasscodeTyped: (String, String?) -> Unit = { _, _ ->},
    sendOtpOnClick: () -> Unit = { },
    confirmOnClick: () -> Unit = { },
) {
    val size = E2MTheme.alias.size
    val color = E2MTheme.alias.color
    val style = E2MTheme.typography

    if (openDialog.value) {
        E2MDialog(
            modifier = modifier,
            onDismissRequest = { openDialog.value = false },
            title = getString().authenticateOtpTxt,
            content = {
                Column(
                    modifier = Modifier
                        .padding(horizontal = size.spacing.small)
                        .padding(bottom = size.spacing.small),
                    verticalArrangement = Arrangement.spacedBy(size.spacing.small),
                ) {
                    Text(
                        text = "${getString().contentAuthenticateOtp1Txt} $email ${getString().contentAuthenticateOtp2Txt}",
                        style = style.small.regular,
                        color = color.text.white,
                        textAlign = TextAlign.Center,
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

                    E2MButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = size.spacing.small2x),
                        title = getString().confirmTxt,
                        style = Gradient,
                        onClick = { confirmOnClick() }
                    )
                }
            }
        )
    }
}
