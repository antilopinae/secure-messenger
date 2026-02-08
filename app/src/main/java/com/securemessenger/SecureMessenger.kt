package com.securemessenger

import android.app.*
import com.securemessenger.di.appModule
import net.zetetic.database.sqlcipher.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SecureMessenger : Application() {
    override fun onCreate() {
        super.onCreate()

        System.loadLibrary("sqlcipher")
        System.loadLibrary("gobackend")

        startKoin {
            androidContext(this@SecureMessenger)
            modules(appModule)
        }
    }
}