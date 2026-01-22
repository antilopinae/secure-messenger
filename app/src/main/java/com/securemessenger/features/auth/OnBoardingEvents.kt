package com.securemessenger.features.auth

sealed interface OnboardingEvents {
    data class SignUpClick( val status: (status: Boolean) -> Unit) :
        OnboardingEvents

    data class LoginUpClick( val status: (status: Boolean) -> Unit) :
        OnboardingEvents
}
