package com.mobile.e2m.account.presentation.login

sealed interface LoginAction {
    data object LoginClick : LoginAction
    data object GetPassword : LoginAction
    data class OnEmailAccountTyped(val emailAccount: String) : LoginAction
    data class OnPasswordTyped(val password: String) : LoginAction
    data class OnFocusChange(val isFocus: Boolean) : LoginAction
}

data class LoginState(
    val isLoading: Boolean = false,
    val emailAccount: String = "",
    val password: String = "",
    val emailAccountError: Int? = null,
    val passwordError: Int? = null,
    val userId: Int? = null,
)

sealed interface LoginEvent {
    data class GoToMainScreen(val userId: Int) : LoginEvent
    data class FocusInputFieldChange(val isFocus: Boolean) : LoginEvent
}
