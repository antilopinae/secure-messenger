package com.securemessenger.data.repository

import com.securemessenger.data.db.ChatDao
import com.securemessenger.data.db.MessageEntity
import java.util.UUID
import kotlin.Result

interface NetworkClient {
    fun sendMessage(chatId: String, text: String)
}

interface CryptoEncoder {
    val myNodeId: String
    val myDisplayName: String
}

class ChatRepositoryImpl(
    private val dao: ChatDao,
    private val network: NetworkClient,
    private val crypto: CryptoEncoder
) : ChatRepository {

    override fun getMessages(chatId: String) = dao.getMessagesForChat(chatId)

    override suspend fun sendMessage(chatId: String, text: String): MessageEntity {
        val id = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val local = MessageEntity(
            id = id,
            chatId = chatId,
            senderId = crypto.myNodeId,
            senderName = crypto.myDisplayName,
            text = text,
            timestamp = now,
            state = "LOADING"
        )
        dao.insertMessage(local)

        try {
            network.sendMessage(chatId, text)
            val delivered = local.copy(state = "VISIBLE")
            dao.insertMessage(delivered)
            return delivered
        } catch (e: Exception) {
            val failed = local.copy(state = "ERROR")
            dao.insertMessage(failed)
            throw e
        }
    }

    override suspend fun revealMessage(messageId: String): Result<String> {
        return Result.success("TODO: ADD reveal")
    }
}
