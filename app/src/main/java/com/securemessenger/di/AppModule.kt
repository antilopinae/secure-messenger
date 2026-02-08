package com.securemessenger.di

import com.securemessenger.data.db.*
import com.securemessenger.data.go.GoBridgeConnector
import com.securemessenger.data.repository.ChatRepository
import com.securemessenger.data.repository.ChatRepositoryImpl
import com.securemessenger.ui.viewmodel.*
import kotlinx.coroutines.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single<AppDatabase> {
        AppDatabase.getDatabase(
            context = androidContext(),
            scope = get()
        )
    }

    single<ChatDao> {
        get<AppDatabase>().chatDao()
    }

    single { GoBridgeConnector(get(), get()) }

    single<ChatRepository> { ChatRepositoryImpl(get(), get()) }

    viewModel {
        MainActivityViewModel(dao = get())
    }

    viewModel {
        ChatViewModel(
            get(),
            savedStateHandle = get()
        )
    }
}
