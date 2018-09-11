package com.radionov.currencyconverter.data.network

import android.support.annotation.NonNull
import com.radionov.currencyconverter.data.entities.Currency
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * @author Andrey Radionov
 */
interface CurrencyApi {

    @GET("search/photos")
    fun fetchCurrency(@NonNull @Query("q") query: String): Observable<Currency>
}