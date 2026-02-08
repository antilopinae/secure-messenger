package com.securemessenger.data.db

import androidx.room.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY timestamp DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ChatEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParticipant(participant: ParticipantEntity): Long

    @Transaction
    fun insertFullMessage(
        chat: ChatEntity,
        message: MessageEntity,
        participants: List<ParticipantEntity>
    ) {
        runBlocking {
            insertChat(chat)
            insertMessage(message)
            participants.forEach { insertParticipant(it) }
        }
    }

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>>

    @Query(
        """
    SELECT p.* FROM participants p 
    INNER JOIN chat_participants cp ON p.nodeId = cp.nodeId 
    WHERE cp.chatId = :chatId
"""
    )
    suspend fun getParticipantsForChat(chatId: String): List<ParticipantEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChatParticipant(crossRef: ChatParticipantCrossRef)
}