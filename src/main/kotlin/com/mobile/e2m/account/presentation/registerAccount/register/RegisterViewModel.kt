package com.mobile.e2m.account.presentation.registerAccount.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.base.E2MBaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : E2MBaseViewModel<RegisterState, RegisterEvent, RegisterAction>(
    initialState = RegisterState()
) {

    private var countdownJob: Job? = null

    override fun handleAction(action: RegisterAction) {
        when (action) {
            RegisterAction.SendOtpClick -> handleSendOtpClick()
            is RegisterAction.RegisterClick -> handleRegisterClick(action.openDialog)
            is RegisterAction.OnUsernameTyped -> handleOnUsernameTyped(action.username)
            is RegisterAction.OnFullnameTyped -> handleOnFullnameTyped(action.fullname)
            is RegisterAction.OnEmailTyped -> handleOnEmailTyped(action.email)
            is RegisterAction.OnNewPasswordTyped -> handleOnNewPasswordTyped(action.newPassword)
            is RegisterAction.OnConfirmPasswordTyped -> handleOnConfirmPasswordTyped(action.confirmPassword)
            is RegisterAction.OnPasscodeTyped -> handleOnPasscodeTyped(action.passcode)
        }
    }

    private fun handleRegisterClick(openDialog: Boolean) {
        viewModelScope.launch {
            val (usernameMessage, isUsernameValid) = checkUsername(mutableStateFlow.value.username)
            val (fullnameMessage, isFullnameValid) = checkFullname(mutableStateFlow.value.fullname)
            val (emailMessage, isEmailValid) = checkEmail(mutableStateFlow.value.email)
            val (newPasswordMessage, isNewPasswordValid) = checkNewPassword(mutableStateFlow.value.newPassword)
            val (confirmPasswordMessage, isConfirmPasswordValid) = checkConfirmPassword(
                mutableStateFlow.value.newPassword,
                mutableStateFlow.value.confirmPassword,
            )

            if (!isUsernameValid) {
                mutableStateFlow.update {
                    it.copy(
                        usernameError = usernameMessage,
                        fullnameError = null,
                        emailError = null,
                        newPasswordError = null,
                        confirmPasswordError = null
                    )
                }
                return@launch
            }

            if (!isFullnameValid) {
                mutableStateFlow.update {
                    it.copy(
                        fullnameError = fullnameMessage,
                        emailError = null,
                        newPasswordError = null,
                        confirmPasswordError = null
                    )
                }
                return@launch
            }

            if (!isEmailValid) {
                mutableStateFlow.update {
                    it.copy(
                        emailError = emailMessage,
                        newPasswordError = null,
                        confirmPasswordError = null
                    )
                }
                return@launch
            }

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
                it.copy(
                    usernameError = null,
                    fullnameError = null,
                    emailError = null,
                    newPasswordError = null,
                    confirmPasswordError = null
                )
            }

            sendEvent(RegisterEvent.OpenOtpDialog(openDialog = openDialog))

            handleSendOtpClick()
        }
    }

    private fun handleOnUsernameTyped(username: String) {
        mutableStateFlow.update {
            it.copy(
                username = username,
                usernameError = null,
            )
        }
    }

    private fun handleOnFullnameTyped(fullname: String) {
        mutableStateFlow.update {
            it.copy(
                fullname = fullname,
                fullnameError = null,
            )
        }
    }



    private fun handleOnEmailTyped(email: String) {
        mutableStateFlow.update {
            val previousEmail = it.email

            if (previousEmail != email) {
                countdownJob?.cancel()

                it.copy(
                    email = email,
                    previousEmail = email,
                    emailError = null,
                    sendOtpTextResId = R.string.sendToEmail,
                    countdown = 0
                )
            } else {
                it.copy(
                    email = email,
                    emailError = null
                )
            }
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

    private fun handleOnPasscodeTyped(passcode: String) {
        mutableStateFlow.update {
            it.copy(
                passcode = passcode,
                passcodeError = null,
            )
        }
    }

    private fun handleSendOtpClick() {
        viewModelScope.launch {
            val currentEmail = mutableStateFlow.value.email
            val previousEmail = mutableStateFlow.value.previousEmail

            if (currentEmail != previousEmail) {
                countdownJob?.cancel()
                mutableStateFlow.update {
                    it.copy(
                        previousEmail = currentEmail,
                        sendOtpTextResId = R.string.sendToEmail,
                        countdown = 0
                    )
                }
            } else if (mutableStateFlow.value.countdown == 0) {
                mutableStateFlow.update {
                    it.copy(
                        sendOtpTextResId = R.string.sendToEmail,
                        countdown = 60
                    )
                }

                startCountdown()
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                mutableStateFlow.update { it.copy(countdown = i) }
                delay(1000L)
            }

            mutableStateFlow.update {
                it.copy(
                    sendOtpTextResId = R.string.sendToEmail,
                    countdown = 0
                )
            }
        }
    }

    private fun checkUsername(username: String): Pair<Int?, Boolean> {
        val existingUsernames = listOf(
            "phanmanh",
        )

        return when {
            username.isBlank() -> R.string.errorEmptyUsername to false
            username.length < 6 || username.length > 18 -> R.string.errorWrongFormatUsername to false
            !username.matches(Regex("^[a-zA-Z0-9]+$")) -> R.string.errorImpossibleUsername to false
            existingUsernames.any {
                it.equals(
                    username, ignoreCase = true
                )
            } -> R.string.errorHasExistedUsername to false

            else -> null to true
        }
    }

    private fun checkFullname(fullname: String): Pair<Int?, Boolean> {
        return when {
            fullname.isBlank() -> R.string.errorEmptyFullname to false

            else -> null to true
        }
    }

    private fun checkEmail(email: String): Pair<Int?, Boolean> {
        val existingEmails = listOf(
            "phanmanh@gmail.com",
        )

        return when {
            email.isBlank() -> R.string.errorEmptyEmail to false
            !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> R.string.errorWrongFormatEmail to false

            existingEmails.any {
                it.equals(
                    email, ignoreCase = true
                )
            } -> R.string.errorHasExistedEmail to false

            else -> null to true
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
        newPassword: String, confirmPassword: String
    ): Pair<Int?, Boolean> {
        return when {
            confirmPassword.isBlank() -> R.string.errorEmptyConfirmPassword to false
            confirmPassword.length < 6 -> R.string.errorWrongFormatPassword to false
            !newPassword.equals(
                confirmPassword, ignoreCase = false
            ) -> R.string.errorWrongResetPassword to false

            else -> null to true
        }
    }
}
