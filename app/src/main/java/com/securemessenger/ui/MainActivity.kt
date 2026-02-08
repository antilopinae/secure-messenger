package com.securemessenger.ui

import android.os.*
import androidx.activity.compose.*
import androidx.activity.*
import com.securemessenger.data.go.GoBridgeConnector
import com.securemessenger.ui.theme.*
import com.securemessenger.ui.navigation.*
import com.securemessenger.ui.viewmodel.*
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private val bridge: GoBridgeConnector by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bridge.init(
            nodeId = "node_android_1",
            privX = ByteArray(32) { 0x01.toByte() },
            privEd = ByteArray(64) { 0x02.toByte() },
            localKey = 42
        )

        setContent {
            SecureMessengerTheme {
                val viewModel: MainActivityViewModel = koinViewModel()
                AppNavigation(viewModel)
            }
        }
    }
}
