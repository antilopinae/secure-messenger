package com.securemessenger.features.auth

import com.securemessenger.data.model.UserModel

sealed interface OnboardingEvents {
    data class SignUpClick(val userModel: UserModel, val status: (status: Boolean) -> Unit) :
        OnboardingEvents

    data class LoginUpClick(val userModel: UserModel, val status: (status: Boolean) -> Unit) :
        OnboardingEvents

    data class OtpVerificationClick(val otpText: String, val status: (status: Boolean) -> Unit) :
        OnboardingEvents
}
