package com.securemessenger.data.repository

import com.securemessenger.data.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface MessengerRepository {
    fun getMessages(nodeId: String): Flow<List<MessageModel>>;

    suspend fun revealMessage(messageId: String): String
}