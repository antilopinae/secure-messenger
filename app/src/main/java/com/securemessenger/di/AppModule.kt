package com.securemessenger.di

import android.content.*
import com.securemessenger.data.db.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import kotlinx.coroutines.*
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): AppDatabase {
        return AppDatabase.getDatabase(context, scope)
    }

    @Provides
    fun provideChatDao(database: AppDatabase): ChatDao {
        return database.chatDao()
    }
}