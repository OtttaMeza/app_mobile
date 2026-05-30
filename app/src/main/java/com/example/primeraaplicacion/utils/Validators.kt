package com.example.primeraaplicacion.utils

object Validators {
    fun isValidUsername(username: String): Boolean =
        username.isNotBlank() && username.length >= 3

    fun isValidPassword(password: String): Boolean =
        password.length >= 6

    fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
