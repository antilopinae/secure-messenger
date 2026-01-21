package com.securemessenger.features.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securemessenger.data.model.UserModel
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

    var userModel = UserModel()

    fun action(even: OnboardingEvents) {
        _isLoading.value = true
        when (even) {
            is OnboardingEvents.LoginUpClick -> loginClick(even.userModel, even.status)
            is OnboardingEvents.OtpVerificationClick -> otpVerification(even.otpText, even.status)
            is OnboardingEvents.SignUpClick -> signUpClick(even.userModel, even.status)
        }
    }

    private fun loginClick(userModel: UserModel, status: (status: Boolean) -> Unit) {
        this.userModel = userModel
        isFromLogin = true
        try {
            viewModelScope.launch(Dispatchers.IO) {

//                databaseRef.getReference("users")
//                    .orderByChild("mobileNumber")
//                    .equalTo(this@OnboardingViewModel.userModel.mobileNumber)
//                    .addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(datasnap: DataSnapshot) {
//                            if (datasnap.exists()) {
//
//                                sendOtp(this@OnboardingViewModel.userModel, status)
//                            } else {
//                                _isLoading.value = false
//                                Log.e("TAG", "User Dosen't exist.")
//                                _snackBarState.value = _snackBarState.value.copy(
//                                    show = true,
//                                    isError = true,
//                                    message = "User Dosen't exist, Please Sign Up."
//                                )
//                                status(false)
//                            }
//                        }
//
//                        override fun onCancelled(p0: DatabaseError) {
//                            status(false)
//                            _isLoading.value = false
//                            _snackBarState.value = _snackBarState.value.copy(
//                                show = true,
//                                isError = true,
//                                message = p0.message
//                            )
//                            Log.e("TAG", "onCancelled: " + p0.message)
//                        }
//                    })
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

    private fun signUpClick(userModel: UserModel, status: (status: Boolean) -> Unit) {
        this.userModel = userModel
        isFromLogin = false
        viewModelScope.launch(Dispatchers.IO) {

//            databaseRef.getReference("users")
//                .orderByChild("mobileNumber")
//                .equalTo(userModel.mobileNumber)
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(datasnap: DataSnapshot) {
//                        if (datasnap.exists()) {
//                            _isLoading.value = false
//                            _snackBarState.value = _snackBarState.value.copy(
//                                show = true,
//                                isError = true,
//                                message = "User Already exist, Please try to login."
//                            )
//                            Log.e("TAG", "User Already exist.")
//                        } else {
//                            sendOtp(userModel, status)
//                        }
//                    }
//
//                    override fun onCancelled(p0: DatabaseError) {
//                        status(false)
//                        _isLoading.value = false
//                        Log.e("TAG", "onCancelled: " + p0.message)
//                    }
//                })
        }

    }

    private fun otpVerification(code: String, status: (status: Boolean) -> Unit) {
//        val credentials = PhoneAuthProvider.getCredential(storageVerificationId, code)
//        verifyOtp(credentials, status)
    }

    private fun sendOtp(userModel: UserModel, status: (status: Boolean) -> Unit) {

        try {
//            viewModelScope.launch {
//                val number = "+1" + userModel.mobileNumber
//                val option = PhoneAuthOptions.newBuilder(auth)
//                    .setPhoneNumber(number)
//                    .setTimeout(60L, TimeUnit.SECONDS)
//                    .setActivity(activity)
//                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                            status(true)
//                            _isLoading.value = false
//                        }
//
//                        override fun onVerificationFailed(p0: FirebaseException) {
//                            status(false)
//                            _isLoading.value = false
//                        }
//
//                        override fun onCodeSent(
//                            verificationId: String,
//                            token: PhoneAuthProvider.ForceResendingToken
//                        ) {
////                            super.onCodeSent(p0, p1)
//                            storageVerificationId = verificationId
//                            status(true)
//                            _isLoading.value = false
//                        }
//                    })
//                    .build()
//
//                PhoneAuthProvider.verifyPhoneNumber(option)
//
//            }
        } catch (e: Exception) {
            status(false)
            _isLoading.value = false
        }
    }

//    private fun verifyOtp(credentials: PhoneAuthCredential, status: (status: Boolean) -> Unit) {
//        try {
//            viewModelScope.launch {
//                auth.signInWithCredential(credentials)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            if (isFromLogin) {
//                                status(true)
//                                _isLoading.value = false
//                                return@addOnCompleteListener
//                            }
//                            viewModelScope.launch {
//
////                                val key = databaseRef.getReference("users").push().key
//                                val key = FirebaseAuth.getInstance().currentUser?.uid
//                                key?.let {
//                                    userModel = userModel.copy(userId = it)
//                                    databaseRef.getReference("users").child(it).setValue(userModel)
//                                        .addOnSuccessListener {
//                                            status(true)
//                                            _isLoading.value = false
//                                        }
//                                        .addOnFailureListener {
//                                            status(false)
//                                            _isLoading.value = false
//                                        }
//                                }
//                            }
//                        } else {
//                            status(false)
//                            _isLoading.value = false
//                        }
//                    }
//
//            }
//
//        } catch (e: Exception) {
//            _isLoading.value = false
//        }
}