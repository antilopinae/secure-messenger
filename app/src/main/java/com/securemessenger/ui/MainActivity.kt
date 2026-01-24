package com.securemessenger.ui

import android.os.*
import androidx.activity.compose.*
import androidx.activity.*
import androidx.hilt.lifecycle.viewmodel.compose.*
import com.securemessenger.ui.theme.*
import com.securemessenger.ui.navigation.*
import com.securemessenger.ui.viewmodel.*
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureMessengerTheme {
                val viewModel: MainActivityViewModel = hiltViewModel()
                AppNavigation(viewModel)
            }
        }
    }
}
