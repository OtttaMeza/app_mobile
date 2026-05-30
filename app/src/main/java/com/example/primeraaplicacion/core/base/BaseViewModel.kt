package com.example.primeraaplicacion.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.primeraaplicacion.core.error.AppError

abstract class BaseViewModel : ViewModel() {
    private val _error = MutableLiveData<AppError?>()
    val error: LiveData<AppError?> = _error

    protected fun setError(error: AppError) {
        _error.value = error
    }

    protected fun clearError() {
        _error.value = null
    }
}
