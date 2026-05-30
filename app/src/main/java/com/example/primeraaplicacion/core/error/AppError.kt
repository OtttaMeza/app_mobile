package com.example.primeraaplicacion.core.error

sealed class AppError(val message: String) {
    class NetworkError(message: String = "Sin conexión a internet") : AppError(message)
    class ServerError(message: String = "Error en el servidor") : AppError(message)
    class UnknownError(message: String = "Error desconocido") : AppError(message)
}

object ErrorHandler {
    fun handle(throwable: Throwable): AppError = when (throwable) {
        is java.net.UnknownHostException -> AppError.NetworkError()
        is java.net.SocketTimeoutException -> AppError.NetworkError("Tiempo de espera agotado")
        else -> AppError.UnknownError(throwable.message ?: "Error desconocido")
    }
}
