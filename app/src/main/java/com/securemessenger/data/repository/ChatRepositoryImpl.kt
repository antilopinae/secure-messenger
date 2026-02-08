package com.securemessenger.data.repository

import com.securemessenger.data.db.*
import com.securemessenger.data.go.GoBridgeConnector
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PeerJson(val ID: String, val XPub: String, val EdPub: String)

class ChatRepositoryImpl(
    private val dao: ChatDao,
    private val bridge: GoBridgeConnector
) : ChatRepository {

    override fun getMessages(chatId: String) = dao.getMessagesForChat(chatId)

    override suspend fun sendMessage(chatId: String, text: String): MessageEntity {
        val messageId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()

        // 1. Сохраняем локально (статус по умолчанию ERROR, если что-то пойдет не так)
        val entity = MessageEntity(
            id = messageId, chatId = chatId, senderId = "me",
            senderName = "Me", text = text, timestamp = now, state = "HIDDEN"
        )
        dao.insertMessage(entity)

        try {
            // 2. Достаем участников именно ЭТОГО чата из БД
            val participants = dao.getParticipantsForChat(chatId).map {
                PeerJson(
                    ID = it.nodeId,
                    XPub = it.x25519PublicKey, // В Go ожидается строка/байт в зависимости от сериализации
                    EdPub = it.ed25519PublicKey
                )
            }

            if (participants.isEmpty()) {
                throw Exception("No participants found for this chat")
            }

            // 3. Сериализуем в JSON для Go-моста
            val peersJson = Json.encodeToString(participants)

            // 4. Отправляем через Go
            // Параметр 'k' (порог) можно вынести в настройки чата, пока поставим 2
            bridge.getClient()?.sendMessage(text, chatId, peersJson, 2)

            // 5. Если успешно — обновляем статус
            val updated = entity.copy(state = "VISIBLE")
            dao.insertMessage(updated)
            return updated

        } catch (e: Exception) {
            dao.insertMessage(entity.copy(state = "ERROR"))
            throw e
        }
    }

    override suspend fun revealMessage(messageId: String): Result<String> {
        // Здесь будет логика запроса частиц у соседей через Go
        return Result.success("Decrypted via Go")
    }
}