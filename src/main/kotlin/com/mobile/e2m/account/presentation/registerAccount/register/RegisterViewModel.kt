package com.mobile.e2m.account.presentation.registerAccount.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mobile.e2m.account.domain.repository.UsersRepository
import com.mobile.e2m.core.datasource.local.room.entity.UsersEntity
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.base.E2MBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class RegisterViewModel(
    private val usersRepository: UsersRepository,
) : E2MBaseViewModel<RegisterState, RegisterEvent, RegisterAction>(
    initialState = RegisterState(
        viewState = RegisterState.ViewState.Loading
    )
) {

    private var existingUsernames: List<String> = listOf()
    private var existingEmails: List<String> = listOf()
    private var countdownJob: Job? = null
    private var verificationCode: String = ""

    init {
        viewModelScope.launch {
            existingUsernames = usersRepository.getUsername().first()
            existingEmails = usersRepository.getEmail().first()

            Log.d("EManh Debug", "Existing Usernames: $existingUsernames")
            Log.d("EManh Debug", "Existing Emails: $existingEmails")
        }
    }

    override fun handleAction(action: RegisterAction) {
        when (action) {
            RegisterAction.SendOtpClick -> handleSendOtpClick()
            is RegisterAction.ConfirmClick -> handleNextScreenClick(action.openDialog)
            is RegisterAction.RegisterClick -> handleRegisterClick(action.openDialog)
            is RegisterAction.OnUsernameTyped -> handleOnUsernameTyped(action.username)
            is RegisterAction.OnFullnameTyped -> handleOnFullnameTyped(action.fullname)
            is RegisterAction.OnEmailTyped -> handleOnEmailTyped(action.email)
            is RegisterAction.OnNewPasswordTyped -> handleOnNewPasswordTyped(action.newPassword)
            is RegisterAction.OnConfirmPasswordTyped -> handleOnConfirmPasswordTyped(action.confirmPassword)
            is RegisterAction.OnPasscodeTyped -> handleOnPasscodeTyped(action.passcode)
            is RegisterAction.Internal -> handleInternalAction(action)
        }
    }

    private fun handleInternalAction(action: RegisterAction.Internal) {
        when (action) {
            is RegisterAction.Internal.HandleUsersData -> {
                mutableStateFlow.update {
                    it.copy(
                        viewState = RegisterState.ViewState.Content(action.usersData)
                    )
                }
            }
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
                        confirmPasswordError = null,
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
                        confirmPasswordError = null,
                    )
                }
                return@launch
            }

            if (!isEmailValid) {
                mutableStateFlow.update {
                    it.copy(
                        emailError = emailMessage,
                        newPasswordError = null,
                        confirmPasswordError = null,
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
                    passcode = "",
                    usernameError = null,
                    fullnameError = null,
                    emailError = null,
                    newPasswordError = null,
                    confirmPasswordError = null,
                    passcodeError = null,
                )
            }

            sendEvent(RegisterEvent.OpenOtpDialog(openDialog = openDialog))

            handleSendOtpClick()
        }
    }

    private fun handleNextScreenClick(openDialog: Boolean) {
        viewModelScope.launch {
            val (passcodeMessage, isPasscodeValid) = checkPasscode(
                mutableStateFlow.value.passcode, verificationCode
            )

            if (!isPasscodeValid) {
                mutableStateFlow.update {
                    it.copy(
                        passcodeError = passcodeMessage,
                        openDialog = true,
                    )
                }
                return@launch
            }

            mutableStateFlow.update {
                it.copy(
                    passcodeError = null,
                    openDialog = false,
                )
            }

            val newUser = UsersEntity(
                username = mutableStateFlow.value.username,
                fullname = mutableStateFlow.value.fullname,
                email = mutableStateFlow.value.email,
                password = mutableStateFlow.value.newPassword,
            )

            val userId = usersRepository.insertUser(newUser)

            if (userId > 0) {
                sendEvent(RegisterEvent.GoToRegistrationSuccess(openDialog = openDialog))
                Log.e("EManh Debug", "Register Account: Registration success!")
            } else {
                Log.e("EManh Debug", "Register Account: Registration failed!")
            }
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
                    email = email, emailError = null
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
                verificationCode = generateVerificationCode()

                sendPasscodeToMail(
                    email = mutableStateFlow.value.email,
                    fullname = mutableStateFlow.value.fullname,
                    verificationCode = verificationCode
                )

                mutableStateFlow.update {
                    it.copy(
                        sendOtpTextResId = R.string.sendToEmail, countdown = 60
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
                    sendOtpTextResId = R.string.sendToEmail, countdown = 0
                )
            }
        }
    }

    private fun checkUsername(username: String): Pair<Int?, Boolean> {
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

    private fun checkPasscode(passcode: String, verificationCode: String): Pair<Int?, Boolean> {
        return when {
            passcode.isBlank() -> R.string.errorEmptyPasscode to false
            !verificationCode.equals(
                passcode, ignoreCase = true
            ) -> R.string.errorWrongPasscode to false

            else -> null to true
        }
    }

    private fun sendPasscodeToMail(email: String, fullname: String, verificationCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val senderEmail = "phankhacmanh6903@gmail.com"
            val senderPassword = "kejx grwa fwxx zkcj"
            val stringHost = "smtp.gmail.com"

            val properties = Properties().apply {
                put("mail.smtp.host", stringHost)
                put("mail.smtp.port", "465")
                put("mail.smtp.ssl.enable", "true")
                put("mail.smtp.auth", "true")
            }

            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(senderEmail))
                    addRecipient(Message.RecipientType.TO, InternetAddress(email))
                    subject = "[E2Music] Xác minh địa chỉ email của bạn"

                    val emailContent = """
                        <html>
                        <body style="font-family: Arial, sans-serif; font-size: 14px; color: #333;">
                            <p>Xin chào <b>$fullname</b>,</p>
                            <p>Cảm ơn bạn đã đăng ký tài khoản trên <b>E2Music</b>! Để hoàn tất quá trình đăng ký và bảo vệ tài khoản của bạn, vui lòng nhập mã xác nhận dưới đây:</p>
                            <p style="font-size: 18px; font-weight: bold; color: #d32f2f;">✨ Mã xác nhận của bạn là: <span style="font-size: 22px;">$verificationCode</span></p>
                            <p>Mã này có hiệu lực trong <b>10 phút</b>. Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>
                            <p>Nếu có bất kỳ vấn đề nào, hãy liên hệ với chúng tôi qua <a href="mailto:phankhacmanh2n@gmail.com">phankhacmanh2n@gmail.com</a>.</p>
                            <p>🎵 <b>E2Music – Trải nghiệm âm nhạc không giới hạn!</b></p>
                        </body>
                        </html>
                    """.trimIndent()

                    setContent(emailContent, "text/html; charset=UTF-8")
                }

                Transport.send(message)
                Log.e("EManh Debug", "Send To Mail: Email sent successfully!")
            } catch (e: MessagingException) {
                e.printStackTrace()
                Log.e("EManh Debug", "Send To Mail: Email was sent unsuccessfully!")
            }
        }
    }

    private fun generateVerificationCode(): String {
        return (10000..99999).random().toString()
    }
}
