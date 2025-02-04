package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import androidx.lifecycle.viewModelScope
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.base.E2MBaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForgotPasswordViewModel
    : E2MBaseViewModel<ForgotPasswordState, ForgotPasswordEvent, ForgotPasswordAction>(
        initialState = ForgotPasswordState()
    ) {

    private var countdownJob: Job? = null

    override fun handleAction(action: ForgotPasswordAction) {
        when (action) {
            ForgotPasswordAction.SendOtpClick -> handleSendOtpClick()
        }
    }

    private fun handleSendOtpClick() {
        viewModelScope.launch {
            mutableStateFlow.emit(state.copy(
                sendOtpTextResId = R.string.resendCodeLater,
                countdown = 60
            ))

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

            mutableStateFlow.emit(state.copy(
                sendOtpTextResId = R.string.sendToEmail,
                countdown = 0
            ))
        }
    }
}
