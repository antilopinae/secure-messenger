package com.securemessenger.data.repository

import com.securemessenger.data.db.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(chatId: String, text: String): MessageEntity
    suspend fun revealMessage(messageId: String): Result<String>
    fun getMessages(chatId: String): Flow<List<MessageEntity>>
}
