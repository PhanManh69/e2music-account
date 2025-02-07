package com.mobile.e2m.account.presentation.passwordRecovery.resetPassword

sealed interface ResetPasswordAction {
    data object ConfirmClick : ResetPasswordAction
    data class OnNewPasswordTyped(val newPassword: String) : ResetPasswordAction
    data class OnConfirmPasswordTyped(val confirmPassword: String) : ResetPasswordAction
}

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val newPassword: String = "",
    val confirmPassword: String = "",
    val newPasswordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val userId: Int? = null,
)

sealed interface ResetPasswordEvent {
    data object GoToLoginScreen : ResetPasswordEvent
}
