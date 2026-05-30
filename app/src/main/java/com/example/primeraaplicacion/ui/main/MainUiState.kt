package com.example.primeraaplicacion.ui.main

import com.example.primeraaplicacion.domain.model.Screen

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val screens: List<Screen>) : MainUiState()
    data class Error(val message: String) : MainUiState()
}
