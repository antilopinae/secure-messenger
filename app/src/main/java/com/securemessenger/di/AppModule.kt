package com.securemessenger.di

import android.content.Context
import com.securemessenger.data.db.*
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

    viewModel {
        MainActivityViewModel(dao = get())
    }

    viewModel {
        ChatViewModel(
            chatDao = get(),
            savedStateHandle = get()
        )
    }
}
