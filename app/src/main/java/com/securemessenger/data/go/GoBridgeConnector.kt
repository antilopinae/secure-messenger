package com.securemessenger.data.go

import android.util.Log
import com.securemessenger.data.db.ChatDao
import com.securemessenger.data.db.MessageEntity
import pkg.MobileClient
import pkg.MessengerCallback
import pkg.Pkg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class GoBridgeConnector(
    private val dao: ChatDao,
    private val scope: CoroutineScope
) : MessengerCallback {

    private var client: MobileClient? = null

    fun init(nodeId: String, privX: ByteArray, privEd: ByteArray, localKey: Int) {
        try {
            client = Pkg.newClient(
                nodeId,
                privX,
                privEd,
                localKey as Long,
                "10.0.2.2:50051", // Адрес сервера для эмулятора
                this
            )
            client?.listen()
        } catch (e: Exception) {
            Log.e("GoBridge", "Failed to init: ${e.message}")
        }
    }

    fun getClient() = client

    // Вызывается из Go, когда пришел MessagePacket (Слой 4)
    override fun onPacketReceived(from: String?, payload: ByteArray?) {
        scope.launch(Dispatchers.IO) {
            // В этой версии для теста мы просто логируем получение.
            // В полной версии здесь вызывается TangleIncoming и запись в БД.
            Log.d("GoBridge", "Received packet from $from, size: ${payload?.size}")
        }
    }

    override fun onLog(msg: String?) {
        Log.i("GoBridge", msg ?: "empty log")
    }
}