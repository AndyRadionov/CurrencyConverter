package com.radionov.currencyconverter.data.network

import android.support.annotation.NonNull
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * @author Andrey Radionov
 */
interface CurrencyApi {

    @GET("convert?compact=ultra")
    fun fetchCurrency(@NonNull @Query("q") query: String): Observable<Map<String, Double>>
}