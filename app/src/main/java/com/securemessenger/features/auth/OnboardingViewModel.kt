package com.securemessenger.features.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securemessenger.ui.component.SnackBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {
    lateinit var activity: Activity

    var storageVerificationId: String = ""
    private var isFromLogin = false

    private val _snackBarState = MutableStateFlow(SnackBarState())
    val snackBarState: StateFlow<SnackBarState> = _snackBarState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun provideActivity(activity: Activity) {
        this.activity = activity

    }

    fun action(even: OnboardingEvents) {
        _isLoading.value = true
        when (even) {
            is OnboardingEvents.LoginUpClick -> loginClick(even.status)
            is OnboardingEvents.SignUpClick -> signUpClick(even.status)
        }
    }

    private fun loginClick(status: (status: Boolean) -> Unit) {
        isFromLogin = true
        try {
            viewModelScope.launch(Dispatchers.IO) {
            }
        } catch (e: Exception) {
            status(false)
            _isLoading.value = false
            _snackBarState.value = _snackBarState.value.copy(
                show = true,
                isError = true,
                message = e.message ?: ""
            )
            Log.e("TAG", "onCancelled: " + e.message)
        }
    }

    private fun signUpClick(status: (status: Boolean) -> Unit) {
        isFromLogin = false
        viewModelScope.launch(Dispatchers.IO) {
        }
    }
}