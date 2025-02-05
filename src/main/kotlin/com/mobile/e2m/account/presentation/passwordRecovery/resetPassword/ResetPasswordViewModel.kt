package com.mobile.e2m.account.presentation.passwordRecovery.resetPassword

import androidx.lifecycle.viewModelScope
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.base.E2MBaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel
    : E2MBaseViewModel<ResetPasswordState, ResetPasswordEvent, ResetPasswordAction>(
    initialState = ResetPasswordState()
) {
    override fun handleAction(action: ResetPasswordAction) {
        when (action) {
            ResetPasswordAction.ConfirmClick -> handleNextScreenClick()
            is ResetPasswordAction.OnNewPasswordTyped -> handleOnNewPasswordTyped(action.newPassword)
            is ResetPasswordAction.OnConfirmPasswordTyped -> handleOnConfirmPasswordTyped(action.confirmPassword)
        }
    }

    private fun handleNextScreenClick() {
        viewModelScope.launch {
            val (newPasswordMessage, isNewPasswordValid) = checkNewPassword(mutableStateFlow.value.newPassword)
            val (confirmPasswordMessage, isConfirmPasswordValid) = checkConfirmPassword(
                mutableStateFlow.value.newPassword,
                mutableStateFlow.value.confirmPassword,
            )

            if (!isNewPasswordValid) {
                mutableStateFlow.update {
                    it.copy(newPasswordError = newPasswordMessage, confirmPasswordError = null)
                }
                return@launch
            }

            if (!isConfirmPasswordValid) {
                mutableStateFlow.update {
                    it.copy(confirmPasswordError = confirmPasswordMessage)
                }
                return@launch
            }

            mutableStateFlow.update {
                it.copy(newPasswordError = null, confirmPasswordError = null)
            }

            sendEvent(ResetPasswordEvent.GoToLoginScreen)
        }
    }

    private fun handleOnNewPasswordTyped(newPassword: String) {
        mutableStateFlow.update {
            it.copy(
                newPassword = newPassword,
                newPasswordError = null,
            )
        }
    }

    private fun handleOnConfirmPasswordTyped(confirmPassword: String) {
        mutableStateFlow.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = null,
            )
        }
    }

    private fun checkNewPassword(newPassword: String): Pair<Int?, Boolean> {
        return when {
            newPassword.isBlank() -> R.string.errorEmptyNewPassword to false
            newPassword.length < 6 -> R.string.errorWrongFormatPassword to false
            !newPassword.any { it.isUpperCase() } -> R.string.errorMissingUpperCase to false
            !newPassword.any { it.isDigit() } -> R.string.errorMissingLetterDigit to false
            else -> null to true
        }
    }

    private fun checkConfirmPassword(
        newPassword: String,
        confirmPassword: String
    ): Pair<Int?, Boolean> {
        return when {
            confirmPassword.isBlank() -> R.string.errorEmptyConfirmPassword to false
            confirmPassword.length < 6 -> R.string.errorWrongFormatPassword to false
            !newPassword.equals(
                confirmPassword,
                ignoreCase = false
            ) -> R.string.errorWrongResetPassword to false

            else -> null to true
        }
    }
}
