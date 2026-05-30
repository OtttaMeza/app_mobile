package com.example.primeraaplicacion.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.primeraaplicacion.data.local.ScreenLocalDataSource
import com.example.primeraaplicacion.data.remote.ScreenRemoteDataSource
import com.example.primeraaplicacion.data.repository.ScreenRepositoryImpl
import com.example.primeraaplicacion.domain.usecase.GetScreensUseCase

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val localDataSource = ScreenLocalDataSource()
        val remoteDataSource = ScreenRemoteDataSource()
        val repository = ScreenRepositoryImpl(remoteDataSource, localDataSource)
        val useCase = GetScreensUseCase(repository)
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(useCase) as T
    }
}
