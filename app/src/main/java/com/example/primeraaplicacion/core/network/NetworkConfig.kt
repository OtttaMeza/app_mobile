package com.example.primeraaplicacion.core.network

import com.example.primeraaplicacion.core.constants.AppConstants

object NetworkConfig {
    val baseUrl: String get() = AppConstants.BASE_URL
    val timeoutSeconds: Long get() = AppConstants.TIMEOUT_SECONDS

    // Para integrar Retrofit agregar dependencia y descomentar:
    // fun provideRetrofit(): Retrofit = Retrofit.Builder()
    //     .baseUrl(baseUrl)
    //     .client(OkHttpClient.Builder().connectTimeout(timeoutSeconds, TimeUnit.SECONDS).build())
    //     .addConverterFactory(GsonConverterFactory.create())
    //     .build()
}
