package com.securemessenger.ui.component

data class SnackBarState(
    val show : Boolean = false,
    val message : String = "",
    val isError : Boolean = false
)
