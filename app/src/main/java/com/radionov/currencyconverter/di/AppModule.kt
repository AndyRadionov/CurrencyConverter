package com.radionov.currencyconverter.di

import android.support.annotation.NonNull
import com.google.gson.Gson
import com.radionov.currencyconverter.BuildConfig
import com.radionov.currencyconverter.data.network.CurrencyApi
import com.radionov.currencyconverter.data.repositories.CurrencyRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Andrey Radionov
 */
@Module
class AppModule {

    @NonNull
    @Provides
    @Singleton
    fun provideCurrencyRepository(currencyApi: CurrencyApi): CurrencyRepository {

        return CurrencyRepository(currencyApi)
    }

    @NonNull
    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi {

        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(CurrencyApi::class.java)
    }
}