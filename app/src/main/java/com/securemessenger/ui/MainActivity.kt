package com.securemessenger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.securemessenger.ui.theme.SecureMessengerTheme
import com.securemessenger.ui.viewmodel.MainActivityViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainActivityContent(viewModel)
        }
    }
}

@Composable
fun MainActivityContent(
    viewModel: MainActivityViewModel
) {
//    val snackBarState by viewModel.snackBarState
//        .collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading
        .collectAsStateWithLifecycle()

    MainActivity(
        isLoading = isLoading,
        onSnackBarShown = /*viewModel::onSnackBarShown*/ {}
    )
}

@Composable
fun MainActivity(
    isLoading: Boolean,
    onSnackBarShown: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    SecureMessengerTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Main Screen")
            }

//            LaunchedEffect(snackBarState.show) {
//                if (snackBarState.show) {
//                    snackBarHostState.showSnackbar(
//                        message = snackBarState.message,
//                        actionLabel = if (snackBarState.isError) "Error" else "Success",
//                        duration = SnackbarDuration.Short
//                    )
//                    onSnackBarShown()
//                }
//            }
    }
        }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MainActivity(
        isLoading = true,
        onSnackBarShown = {}
    )
}

