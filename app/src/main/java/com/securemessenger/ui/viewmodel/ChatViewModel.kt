package com.securemessenger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    private var chatId: String? = null

    fun action() {

    }

    fun setChatId(chatId: String) {
        this.chatId = chatId
    }

//    fun getMessages(callBack: (list: List<MessageModel>) -> Unit) {
//        chatId?.let { id ->
//            viewModelScope.launch(Dispatchers.IO) {
//
//                databaseRef.child("chats").child(id).child("messages")
//                    .orderByChild("timestamp")
//                    .addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            val messages = dataSnapshot.children.mapNotNull { messageSnap ->
//                                messageSnap.getValue(MessageModel::class.java)
//                            }
//                            callBack(messages.reversed())
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//
//                        }
//
//                    })
//
//            }
//        }
//    }

    fun sendMessage(message: String, callBack: (status: Boolean) -> Unit) {
//        var messageInstance = MessageModel(
//            text = message,
//            senderId = currentUser?.uid,
//            timeStamp = System.currentTimeMillis()
//        )

        chatId?.let { id ->
            viewModelScope.launch {

                withContext(Dispatchers.IO) {
                }
            }
        }
    }
}