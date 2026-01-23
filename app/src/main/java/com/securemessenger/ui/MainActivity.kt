package com.securemessenger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.securemessenger.ui.theme.SecureMessengerTheme
import com.securemessenger.ui.viewmodel.MainActivityViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.securemessenger.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecureMessengerTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MainActivity()
}

