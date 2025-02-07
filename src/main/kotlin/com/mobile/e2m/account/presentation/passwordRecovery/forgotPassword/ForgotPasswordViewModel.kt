package com.mobile.e2m.account.presentation.passwordRecovery.forgotPassword

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mobile.e2m.account.domain.repository.UsersRepository
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

class ForgotPasswordViewModel(
    private val usersRepository: UsersRepository,
) : E2MBaseViewModel<ForgotPasswordState, ForgotPasswordEvent, ForgotPasswordAction>(
    initialState = ForgotPasswordState()
) {

    private var existingUsernames: List<String> = emptyList()
    private var existingEmails: List<String> = emptyList()
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
            val (emailMessage, isEmailValid) = checkEmailAccount(mutableStateFlow.value.emailAccount)

            if (!isEmailValid) {
                mutableStateFlow.update {
                    it.copy(emailAccountError = emailMessage, passcodeError = null)
                }
                return@launch
            }

            val getUser = usersRepository
                .getUserByEmailOrUsername(emailAccount = mutableStateFlow.value.emailAccount)
                .first()

            Log.e("EManh Debug", "Get Users: $getUser")

            verificationCode = generateVerificationCode()

            sendPasscodeToMail(
                email = getUser.first().email,
                fullname = getUser.first().fullname,
                verificationCode = verificationCode
            )

            mutableStateFlow.emit(
                state.copy(
                    sendOtpTextResId = R.string.sendToEmail,
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
            val (emailMessage, isEmailValid) = checkEmailAccount(mutableStateFlow.value.emailAccount)
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

            val getUser = usersRepository
                .getUserByEmailOrUsername(emailAccount = mutableStateFlow.value.emailAccount)
                .first()

            sendEvent(ForgotPasswordEvent.GoToResetPasswordScreen(getUser.first().id))
        }
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
                            <p>Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản của mình trên <b>E2Music</b>! Để tiếp tục, vui lòng sử dụng mã xác nhận dưới đây:</p>
                            <p style="font-size: 18px; font-weight: bold; color: #d32f2f;">🔑 Mã xác nhận của bạn là: <span style="font-size: 22px;">$verificationCode</span></p>
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
