package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import com.mobile.e2m.core.ui.R

sealed interface ForgotPasswordAction {
    data object SendOtpClick : ForgotPasswordAction
    data object ConfirmClick : ForgotPasswordAction
    data class OnEmailTyped(val emailAccount: String) : ForgotPasswordAction
    data class OnPasscodeTyped(val passcode: String) : ForgotPasswordAction
}

data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val sendOtpTextResId: Int = R.string.sendToEmail,
    val countdown: Int = 0,
    val emailAccount: String = "",
    val passcode: String = "",
    val emailAccountError: Int? = null,
    val passcodeError: Int? = null,
)

sealed interface ForgotPasswordEvent {
    data object GoToResetPasswordScreen : ForgotPasswordEvent
}
