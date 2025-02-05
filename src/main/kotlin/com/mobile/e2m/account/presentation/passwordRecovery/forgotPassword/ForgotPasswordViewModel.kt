package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import androidx.lifecycle.viewModelScope
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.base.E2MBaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel
    : E2MBaseViewModel<ForgotPasswordState, ForgotPasswordEvent, ForgotPasswordAction>(
    initialState = ForgotPasswordState()
) {

    private var countdownJob: Job? = null

    override fun handleAction(action: ForgotPasswordAction) {
        when (action) {
            ForgotPasswordAction.SendOtpClick -> handleSendOtpClick()
            ForgotPasswordAction.ConfirmClick -> handleNextScreenClick()
            is ForgotPasswordAction.OnEmailTyped -> handleOnEmailTyped(action.emailAccount)
            is ForgotPasswordAction.OnPasscodeTyped -> handleOnPasscodeTyped(action.passcode)
        }
    }

    private fun handleSendOtpClick() {
        viewModelScope.launch {
            mutableStateFlow.emit(
                state.copy(
                    sendOtpTextResId = R.string.resendCodeLater,
                    countdown = 60
                )
            )

            startCountdown()
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                mutableStateFlow.emit(state.copy(countdown = i))
                delay(1000L)
            }

            mutableStateFlow.emit(
                state.copy(
                    sendOtpTextResId = R.string.sendToEmail,
                    countdown = 0
                )
            )
        }
    }

    private fun handleOnEmailTyped(emailAccount: String) {
        mutableStateFlow.update {
            it.copy(
                emailAccount = emailAccount,
                emailAccountError = null,
            )
        }
    }

    private fun handleOnPasscodeTyped(passcode: String) {
        mutableStateFlow.update {
            it.copy(
                passcode = passcode,
                passcodeError = null,
            )
        }
    }

    private fun handleNextScreenClick() {
        viewModelScope.launch {
            val verificationCode = "12345"
            val (emailMessage, isEmailValid) = checkEmail(mutableStateFlow.value.emailAccount)
            val (passcodeMessage, isPasscodeValid) = checkPasscode(
                mutableStateFlow.value.passcode,
                verificationCode
            )

            if (!isEmailValid) {
                mutableStateFlow.update {
                    it.copy(emailAccountError = emailMessage, passcodeError = null)
                }
                return@launch
            }

            if (!isPasscodeValid) {
                mutableStateFlow.update {
                    it.copy(passcodeError = passcodeMessage)
                }
                return@launch
            }

            mutableStateFlow.update {
                it.copy(emailAccountError = null, passcodeError = null)
            }

            sendEvent(ForgotPasswordEvent.GoToResetPasswordScreen)
        }
    }

    private fun checkEmail(emailAccount: String): Pair<Int?, Boolean> {
        return when {
            emailAccount.isBlank() -> R.string.errorEmptyEmailAcc to false
//            "" -> R.string.errorNotEmailAcc to false
            else -> null to true
        }
    }

    private fun checkPasscode(passcode: String, verificationCode: String): Pair<Int?, Boolean> {
        return when {
            passcode.isBlank() -> R.string.errorEmptyPasscode to false
            !verificationCode.equals(
                passcode,
                ignoreCase = true
            ) -> R.string.errorWrongPasscode to false

            else -> null to true
        }
    }
}
