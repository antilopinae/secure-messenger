package com.securemessenger.data.db

import android.content.*
import androidx.room.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.count
import net.zetetic.database.sqlcipher.*

@Database(
    entities = [ParticipantEntity::class, ChatEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val password = "super_secret_password"
                val passphrase = password.toByteArray(Charsets.UTF_8)

                val factory = SupportOpenHelperFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "secure_messenger.db"
                ).openHelperFactory(factory).fallbackToDestructiveMigration().build()

                INSTANCE = instance

                scope.launch(Dispatchers.IO) {
                    if (instance.chatDao().getAllChats().count() == 0) {
                        seedDatabase(instance.chatDao())
                    }
                }

                instance
            }
        }

        private suspend fun seedDatabase(dao: ChatDao) {
            val p1 = ParticipantEntity("node_alice", "Alice", "pub_ed_alice", "pub_curv_alice")
            val p2 = ParticipantEntity("node_bob", "Bob", "pub_ed_bob", "pub_curv_bob")
            val p3 =
                ParticipantEntity("node_charlie", "Charlie", "pub_ed_charlie", "pub_curv_charlie")

            dao.insertParticipant(p1)
            dao.insertParticipant(p2)
            dao.insertParticipant(p3)

            val mainChat = ChatEntity(
                "chat_main",
                "SECURE CORE GROUP",
                "Encrypted data packet...",
                System.currentTimeMillis()
            )
            dao.insertChat(mainChat)

            dao.insertMessage(
                MessageEntity(
                    "m1",
                    "chat_main",
                    "node_alice",
                    "Alice",
                    "System check: Layer 1 particles generated.",
                    System.currentTimeMillis() - 10000,
                    "VISIBLE"
                )
            )
            dao.insertMessage(
                MessageEntity(
                    "m2",
                    "chat_main",
                    "node_bob",
                    "Bob",
                    "Node Bob: Received L2 sub-particles. Stored.",
                    System.currentTimeMillis() - 5000,
                    "HIDDEN"
                )
            )
            dao.insertMessage(
                MessageEntity(
                    "m3",
                    "chat_main",
                    "node_charlie",
                    "Charlie",
                    "Encryption verified. Channel secure.",
                    System.currentTimeMillis(),
                    "VISIBLE"
                )
            )
        }
    }
}