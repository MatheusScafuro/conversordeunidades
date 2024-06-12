package com.example.conversordeunidades

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("latest")
    fun getRates(@Query("base") base: String): Call<ExchangeRatesResponse>
}

data class ExchangeRatesResponse(
    val rates: Map<String, Double>
)
