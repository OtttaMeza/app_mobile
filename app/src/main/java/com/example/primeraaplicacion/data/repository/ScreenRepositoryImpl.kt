package com.example.primeraaplicacion.data.repository

import com.example.primeraaplicacion.data.local.ScreenLocalDataSource
import com.example.primeraaplicacion.data.remote.ScreenRemoteDataSource
import com.example.primeraaplicacion.domain.model.Screen
import com.example.primeraaplicacion.domain.repository.IScreenRepository

class ScreenRepositoryImpl(
    private val remoteDataSource: ScreenRemoteDataSource,
    private val localDataSource: ScreenLocalDataSource
) : IScreenRepository {

    override fun getScreens(): List<Screen> {
        val cached = localDataSource.getScreens()
        if (cached.isNotEmpty()) return cached
        val remote = remoteDataSource.getScreens()
        localDataSource.saveScreens(remote)
        return remote
    }

    override fun getScreenById(id: Int): Screen? =
        localDataSource.getScreens().find { it.id == id }
            ?: remoteDataSource.getScreens().find { it.id == id }
}
