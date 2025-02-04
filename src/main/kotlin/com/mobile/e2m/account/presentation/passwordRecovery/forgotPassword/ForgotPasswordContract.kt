package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import com.mobile.e2m.core.ui.R

sealed interface ForgotPasswordAction {
    data object SendOtpClick : ForgotPasswordAction
}

data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val sendOtpTextResId: Int = R.string.sendToEmail,
    val countdown: Int = 0,
)

sealed interface ForgotPasswordEvent {
}
