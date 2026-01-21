package com.securemessenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securemessenger.data.model.UserModel
import com.securemessenger.ui.component.SnackBarState
import com.securemessenger.ui.event.MainScreenAction
import com.securemessenger.ui.event.MainScreenEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val _snackBarState = MutableStateFlow(SnackBarState())
    private val _mainScreenEvent = MutableStateFlow(
        MainScreenEvent(
            currentUser = UserModel()
        )
    )
    private val _isLoading = MutableStateFlow(false)

    val snackBarState: StateFlow<SnackBarState> = _snackBarState.asStateFlow()
    val chatViewModel: ChatViewModel = ChatViewModel()

    val mainScreenEvent: StateFlow<MainScreenEvent> = _mainScreenEvent.asStateFlow()

    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

//    var chatJob = CompletableJob()

    init {
        loadUserList()
    }

    fun action(event: MainScreenAction) {
        _isLoading.value = true
        when (event) {
            is MainScreenAction.SelectUser -> initChat(event)
            is MainScreenAction.SendMessage -> chatViewModel.sendMessage(event.message) {
                _isLoading.value = false
                event.callBack(it)
            }
        }
    }

    private fun loadUserList() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
//            if (currentUser == null) {
//                return@launch
//            }
//            databaseRef.getReference("users")
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val list = mutableListOf<UserModel>()
//                        Log.d("TAG", "onDataChange: ${snapshot.children.count()}")
//                        for (userSnapshot in snapshot.children) {
//                            val userModel = userSnapshot.getValue(UserModel::class.java)
//                            userModel?.let {
//                                if (it.userId != currentUser.uid) {
//                                    list.add(it)
//                                }
//                            }
//                        }
//                        Log.d("TAG", "onDataChange: ${list.toMutableList().size}")
//                        _mainScreenEvent.value = _mainScreenEvent.value.copy(
//                            userList = list.toMutableList()
//                        )
//                        _isLoading.value = false
//                    }
//
//                    override fun onCancelled(p0: DatabaseError) {
//                        _isLoading.value = false
//                        _snackBarState.value = _snackBarState.value.copy(
//                            show = true,
//                            isError = true,
//                            message = p0.message
//                        )
//                    }
//                })
        }
    }

    private fun initChat(event: MainScreenAction.SelectUser) {
//        _mainScreenEvent.value = _mainScreenEvent.value.copy(
//            selectedUser = event.userModel
//        )

        try {
            viewModelScope.launch(Dispatchers.IO) {

//                if (dataSnapshot.hasChildren()) {
//                    try {
//                        _isLoading.value = false
//                        var chatId: String? = null
//
//                        for (chatSnapshot in dataSnapshot.children) {
//                            val chatUsers = chatSnapshot.child("users").value as? Map<*, *>
//                            if (chatUsers?.containsKey(event.userModel.userId) == true
//                                && chatUsers.containsKey(_mainScreenEvent.value.currentUser?.userId)
//                            ) {
//                                chatId = chatSnapshot.key
//                                _mainScreenEvent.value = _mainScreenEvent.value.copy(
//                                    currentChatId = chatId
//                                )
//                                chatViewModel.setChatId(chatId ?: "")
//                                chatViewModel.getMessages {
//                                    _mainScreenEvent.value = _mainScreenEvent.value.copy(
//                                        messagesList = it
//                                    )
//                                }
//                                break
//                            }
//                        }
//
//                        if (chatId == null) {
//                            chatId = dataSnapshot.ref.push().key
//                            val chatData = mapOf(
//                                "${_mainScreenEvent.value.currentUser?.userId}" to true,
//                                "${event.userModel.userId}" to true,
//                            )
//                            dataSnapshot.ref.child(chatId ?: "").child("users").setValue(chatData)
//                            _mainScreenEvent.value = _mainScreenEvent.value.copy(
//                                currentChatId = chatId
//                            )
//                            chatViewModel.setChatId(chatId ?: "")
//                            chatViewModel.getMessages {
//                                _mainScreenEvent.value = _mainScreenEvent.value.copy(
//                                    messagesList = it
//                                )
//                            }
//                        }
//                    } catch (ex: Exception) {
//                        Log.e("TAG", "onDataChange: ", ex)
//                    }
//
//                } else {
//                    _isLoading.value = false
//                    val chatId = databaseRef.getReference("chats").push().key
//
//                    val chatData = mapOf(
//                        "${_mainScreenEvent.value.currentUser?.userId}" to true,
//                        "${event.userModel.userId}" to true,
//                    )
//                    dataSnapshot.ref.child(chatId ?: "").child("users").setValue(chatData)
//                    _mainScreenEvent.value = _mainScreenEvent.value.copy(
//                        currentChatId = chatId
//                    )
//                }
            }
        } catch (e: Exception) {
            _isLoading.value = false
            _snackBarState.value = _snackBarState.value.copy(
                show = true,
                isError = true,
                message = e.message ?: ""
            )
        }
    }
}