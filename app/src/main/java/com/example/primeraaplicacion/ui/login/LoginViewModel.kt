package com.example.primeraaplicacion.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.primeraaplicacion.core.base.BaseViewModel
import com.example.primeraaplicacion.utils.Validators

class LoginViewModel : BaseViewModel() {

    private val _uiState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        if (!Validators.isValidUsername(username)) {
            _uiState.value = LoginUiState.Error("El usuario debe tener al menos 3 caracteres")
            return
        }
        if (!Validators.isValidPassword(password)) {
            _uiState.value = LoginUiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }
        _uiState.value = LoginUiState.Loading
        // Punto de integración: llamar al use case de autenticación
        _uiState.value = LoginUiState.Success
    }
}
