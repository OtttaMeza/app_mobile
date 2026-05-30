package com.example.primeraaplicacion.data.local

import com.example.primeraaplicacion.domain.model.Screen

class ScreenLocalDataSource {
    // Punto de integración para Room:
    // @Dao interface ScreenDao { @Query("SELECT * FROM screens") fun getAll(): List<ScreenEntity> }
    private val cache = mutableListOf<Screen>()

    fun getScreens(): List<Screen> = cache.toList()

    fun saveScreens(screens: List<Screen>) {
        cache.clear()
        cache.addAll(screens)
    }
}
