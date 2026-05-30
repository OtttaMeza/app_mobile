package com.example.primeraaplicacion.data.remote

import com.example.primeraaplicacion.domain.model.Screen

class ScreenRemoteDataSource {
    // Punto de integración para Retrofit:
    // @GET("screens") suspend fun fetchScreens(): Response<List<ScreenDto>>
    fun getScreens(): List<Screen> = listOf(
        Screen(id = 1, title = "Pantalla Principal"),
        Screen(id = 2, title = "Segunda Pantalla")
    )
}
