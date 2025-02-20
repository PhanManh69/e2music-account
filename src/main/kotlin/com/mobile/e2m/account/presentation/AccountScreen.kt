package com.mobile.e2m.account.presentation

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mobile.e2m.account.navigation.accountNavGraph
import com.mobile.e2m.account.router.AccountRouter
import com.mobile.e2m.core.ui.R
import com.mobile.e2m.core.ui.navigation.route.AppNavigationRoute
import org.koin.compose.koinInject

@Immutable
data class GetStrings(
    val titleStartedTxt: String,
    val introduceStartedTxt: String,
    val startTxt: String,
    val loginTxt: String,
    val welcomeToLoginTxt: String,
    val enterAccountEmailTxt: String,
    val enterPasswordTxt: String,
    val passwordRecoveryTxt: String,
    val continueWithTxt: String,
    val loginWithGoogleTxt: String,
    val loginWithFacebookTxt: String,
    val loginWithAppleIDTxt: String,
    val noAccountTxt: String,
    val registerTxt: String,
    val forgotPasswordTxt: String,
    val sendToEmailTxt: String,
    val resendCodeLaterTxt: String,
    val confirmTxt: String,
    val resetPasswordTxt: String,
    val enterNewPasswordTxt: String,
    val enterConfirmPasswordTxt: String,
    val enterUsernameTxt: String,
    val enterFullnameTxt: String,
    val enterEmailTxt: String,
    val authenticateOtpTxt: String,
    val contentAuthenticateOtp1Txt: String,
    val contentAuthenticateOtp2Txt: String,
    val registrationSuccessfulTxt: String,
    val contentRegistrationSuccessfulTxt: String,
    val resetPasswordSuccessfulTxt: String,
    val contentResetPasswordSuccessfulTxt: String,
) {
    companion object {
        @Composable
        internal fun default(
            context: Context = LocalContext.current,
        ) = GetStrings(
            titleStartedTxt = context.getString(R.string.titleStarted),
            introduceStartedTxt = context.getString(R.string.introduceStarted),
            startTxt = context.getString(R.string.start),
            loginTxt = context.getString(R.string.login),
            welcomeToLoginTxt = context.getString(R.string.welcomeToLogin),
            enterAccountEmailTxt = context.getString(R.string.enterAccountEmail),
            enterPasswordTxt = context.getString(R.string.enterPassword),
            passwordRecoveryTxt = context.getString(R.string.passwordRecovery),
            continueWithTxt = context.getString(R.string.continueWith),
            loginWithGoogleTxt = context.getString(R.string.loginWithGoogle),
            loginWithFacebookTxt = context.getString(R.string.loginWithFacebook),
            loginWithAppleIDTxt = context.getString(R.string.loginWithAppleID),
            noAccountTxt = context.getString(R.string.noAccount),
            registerTxt = context.getString(R.string.register),
            forgotPasswordTxt = context.getString(R.string.forgotPassword),
            sendToEmailTxt = context.getString(R.string.sendToEmail),
            resendCodeLaterTxt = context.getString(R.string.resendCodeLater),
            confirmTxt = context.getString(R.string.confirm),
            resetPasswordTxt = context.getString(R.string.resetPassword),
            enterNewPasswordTxt = context.getString(R.string.enterNewPassword),
            enterConfirmPasswordTxt = context.getString(R.string.enterConfirmPassword),
            enterUsernameTxt = context.getString(R.string.enterUsername),
            enterFullnameTxt = context.getString(R.string.enterFullname),
            enterEmailTxt = context.getString(R.string.enterEmail),
            authenticateOtpTxt = context.getString(R.string.authenticateOtp),
            contentAuthenticateOtp1Txt = context.getString(R.string.contentAuthenticateOtp1),
            contentAuthenticateOtp2Txt = context.getString(R.string.contentAuthenticateOtp2),
            registrationSuccessfulTxt = context.getString(R.string.registrationSuccessful),
            contentRegistrationSuccessfulTxt = context.getString(R.string.contentRegistrationSuccessful),
            resetPasswordSuccessfulTxt = context.getString(R.string.resetPasswordSuccessful),
            contentResetPasswordSuccessfulTxt = context.getString(R.string.contentResetPasswordSuccessful),
        )
    }
}

@Composable
internal fun getString(): GetStrings {
    return GetStrings.default()
}

@Composable
fun AccountScreen(
    navController: NavHostController = rememberNavController(),
    accountRouter: AccountRouter = koinInject(),
) {
    NavHost(
        navController = navController,
        startDestination = AppNavigationRoute.Account.Started,
        modifier = Modifier
            .consumeWindowInsets(WindowInsets.navigationBars.only(WindowInsetsSides.Vertical))
            .consumeWindowInsets(WindowInsets.ime),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, tween(700),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, tween(700),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(700),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End, tween(700),
            )
        },
    ) {
        accountNavGraph(
            navController = navController,
            onAccount = {
                accountRouter.onAccount()
            },
            onAccountStarted = {
                accountRouter.onAccount()
            }
        )
    }
}
