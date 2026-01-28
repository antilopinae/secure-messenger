package com.securemessenger

import android.app.*
import com.securemessenger.di.appModule
import net.zetetic.database.sqlcipher.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

//@HiltAndroidApp
class SecureMessenger : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeSQLCipher();

        startKoin {
            androidContext(this@SecureMessenger)
            modules(appModule)
        }
    }

    private fun initializeSQLCipher() {
        System.loadLibrary("sqlcipher")
        val databaseFile = getDatabasePath("test.db")
        databaseFile.mkdirs()

        val password = "super_secret_password"

        val db = SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null, null)

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS messages (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT NOT NULL
            )
        """.trimIndent()
        )

        db.close()
    }
}