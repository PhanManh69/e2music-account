package com.mobile.e2m.account.presentation.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mobile.e2m.account.domain.repository.UsersRepository
import com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword.ForgotPasswordEvent
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.base.E2MBaseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val usersRepository: UsersRepository,
) : E2MBaseViewModel<LoginState, LoginEvent, LoginAction>(
    initialState = LoginState()
) {

    private var existingUsernames: List<String> = emptyList()
    private var existingEmails: List<String> = emptyList()
    private var existingPassword: String = ""

    init {
        viewModelScope.launch {
            existingUsernames = usersRepository.getUsername().first()
            existingEmails = usersRepository.getEmail().first()

            Log.d("EManh Debug", "Existing Usernames: $existingUsernames")
            Log.d("EManh Debug", "Existing Emails: $existingEmails")
        }
    }

    override fun handleAction(action: LoginAction) {
        when (action) {
            LoginAction.LoginClick -> handleLoginClick()
            LoginAction.GetPassword -> handleGetPassword()
            is LoginAction.OnEmailAccountTyped -> handleOnEmailAccTyped(action.emailAccount)
            is LoginAction.OnPasswordTyped -> handleOnPasswordTyped(action.password)
            is LoginAction.OnFocusChange -> handleOnFocusChange(action.isFocus)
        }
    }

    private fun handleLoginClick() {
        viewModelScope.launch {
            val (emailAccMessage, isEmailAccValid) = checkEmailAccount(mutableStateFlow.value.emailAccount)
            val (passwordMessage, isPasswordValid) = checkPassword(mutableStateFlow.value.password)

            if (!isEmailAccValid) {
                mutableStateFlow.update {
                    it.copy(emailAccountError = emailAccMessage, passwordError = null)
                }
                return@launch
            }

            if (!isPasswordValid) {
                mutableStateFlow.update {
                    it.copy(passwordError = passwordMessage)
                }
                return@launch
            }

            val getUser = usersRepository
                .getUserByEmailOrUsername(emailAccount = mutableStateFlow.value.emailAccount)
                .first()

            mutableStateFlow.update {
                it.copy(emailAccountError = null, passwordError = null)
            }

            sendEvent(LoginEvent.GoToMainScreen(getUser.first().id))
        }
    }

    private fun handleGetPassword() {
        viewModelScope.launch {
            val getUserList = usersRepository
                .getUserByEmailOrUsername(emailAccount = mutableStateFlow.value.emailAccount)
                .firstOrNull()

            val getUser = getUserList?.firstOrNull()

            if (getUser == null) {
                Log.e("EManh Debug", "Get Password: Account not found")
            } else {
                existingPassword = getUser.password
                Log.e("EManh Debug", "Get Password: $existingPassword")
            }
        }
    }

    private fun handleOnEmailAccTyped(emailAccount: String) {
        mutableStateFlow.update {
            it.copy(
                emailAccount = emailAccount,
                emailAccountError = null,
            )
        }
    }

    private fun handleOnPasswordTyped(password: String) {
        mutableStateFlow.update {
            it.copy(
                password = password,
                passwordError = null,
            )
        }
    }

    private fun handleOnFocusChange(focus: Boolean) {
        sendEvent(LoginEvent.FocusInputFieldChange(isFocus = focus))
    }

    private fun checkEmailAccount(emailAccount: String): Pair<Int?, Boolean> {
        val existingEmailAccount = existingEmails + existingUsernames

        return when {
            emailAccount.isBlank() -> R.string.errorEmptyEmailAcc to false
            !existingEmailAccount.any {
                it.equals(
                    emailAccount, ignoreCase = true
                )
            } -> R.string.errorNotEmailAcc to false

            else -> null to true
        }
    }

    private fun checkPassword(password: String): Pair<Int?, Boolean> {
        return when {
            password.isBlank() -> R.string.errorEmptyNewPassword to false
            !password.equals(
                existingPassword, ignoreCase = false
            ) -> R.string.errorWrongPassword to false

            else -> null to true
        }
    }
}
