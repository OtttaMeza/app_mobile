package com.example.primeraaplicacion.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.primeraaplicacion.core.base.BaseViewModel
import com.example.primeraaplicacion.core.error.ErrorHandler
import com.example.primeraaplicacion.domain.usecase.GetScreensUseCase

class MainViewModel(private val getScreensUseCase: GetScreensUseCase) : BaseViewModel() {

    private val _uiState = MutableLiveData<MainUiState>(MainUiState.Loading)
    val uiState: LiveData<MainUiState> = _uiState

    fun loadScreens() {
        _uiState.value = MainUiState.Loading
        runCatching { getScreensUseCase() }
            .onSuccess { _uiState.value = MainUiState.Success(it) }
            .onFailure {
                setError(ErrorHandler.handle(it))
                _uiState.value = MainUiState.Error(it.message ?: "Error desconocido")
            }
    }
}
