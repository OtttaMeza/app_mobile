package com.example.primeraaplicacion.data.mapper

import com.example.primeraaplicacion.domain.model.Screen

object ScreenMapper {
    fun fromMap(raw: Map<String, Any>): Screen = Screen(
        id = (raw["id"] as? Int) ?: 0,
        title = (raw["title"] as? String) ?: ""
    )

    fun toMap(screen: Screen): Map<String, Any> = mapOf(
        "id" to screen.id,
        "title" to screen.title
    )
}
