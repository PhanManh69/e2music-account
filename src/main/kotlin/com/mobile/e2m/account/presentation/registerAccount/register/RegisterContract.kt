package com.mobile.e2m.account.presentation.registerAccount.register

import com.mobile.e2m.account.model.UsersModel
import com.mobile.e2m.core.ui.R

sealed interface RegisterAction {
    data object SendOtpClick : RegisterAction
    data class ConfirmClick(val openDialog: Boolean) : RegisterAction
    data class RegisterClick(val openDialog: Boolean) : RegisterAction
    data class OnUsernameTyped(val username: String) : RegisterAction
    data class OnFullnameTyped(val fullname: String) : RegisterAction
    data class OnEmailTyped(val email: String) : RegisterAction
    data class OnNewPasswordTyped(val newPassword: String) : RegisterAction
    data class OnConfirmPasswordTyped(val confirmPassword: String) : RegisterAction
    data class OnPasscodeTyped(val passcode: String) : RegisterAction

    sealed class Internal : RegisterAction {
        data class HandleUsersData(val usersData: List<UsersModel>?) : Internal()
    }
}

data class RegisterState(
    val isLoading: Boolean = false,
    val username: String = "",
    val fullname: String = "",
    val email: String = "",
    val previousEmail: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val sendOtpTextResId: Int = R.string.sendToEmail,
    val countdown: Int = 0,
    val passcode: String = "",
    val usernameError: Int? = null,
    val fullnameError: Int? = null,
    val emailError: Int? = null,
    val newPasswordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val passcodeError: Int? = null,
    val openDialog: Boolean = true,
    val viewState: ViewState,
) {
    sealed interface ViewState {
        data object Loading : ViewState

        data class Content(
            val newsData: List<UsersModel>?,
        ) : ViewState
    }
}

sealed interface RegisterEvent {
    data class GoToRegistrationSuccess(val openDialog: Boolean) : RegisterEvent
    data class OpenOtpDialog(val openDialog: Boolean) : RegisterEvent
}
