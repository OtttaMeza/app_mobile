package com.example.primeraaplicacion.domain.usecase

import com.example.primeraaplicacion.domain.model.Screen
import com.example.primeraaplicacion.domain.repository.IScreenRepository

class GetScreensUseCase(private val repository: IScreenRepository) {
    operator fun invoke(): List<Screen> = repository.getScreens()
}
