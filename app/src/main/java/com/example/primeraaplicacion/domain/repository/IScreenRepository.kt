package com.example.primeraaplicacion.domain.repository

import com.example.primeraaplicacion.domain.model.Screen

interface IScreenRepository {
    fun getScreens(): List<Screen>
    fun getScreenById(id: Int): Screen?
}
