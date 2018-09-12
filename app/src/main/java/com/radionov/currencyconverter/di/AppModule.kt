package com.radionov.currencyconverter.di

import android.app.Application
import android.support.annotation.NonNull
import android.util.Log
import com.google.gson.Gson
import com.radionov.currencyconverter.BuildConfig
import com.radionov.currencyconverter.data.network.CurrencyApi
import com.radionov.currencyconverter.data.repositories.CurrencyRepository
import com.radionov.currencyconverter.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Andrey Radionov
 */
@Module
class AppModule(private val app: Application) {

    @NonNull
    @Provides
    @Singleton
    fun provideCurrencyRepository(currencyApi: CurrencyApi): CurrencyRepository {

        return CurrencyRepository(currencyApi)
    }

    @NonNull
    @Provides
    @Singleton
    fun provideCurrencyApi(okHttpClient: OkHttpClient): CurrencyApi {

        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(CurrencyApi::class.java)
    }

    @NonNull
    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(initOfflineCacheInterceptor())
                .addNetworkInterceptor(initCacheInterceptor())
                .cache(initCache())
                .build()
    }

    private fun initCache(): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(File(app.cacheDir, "http-cache"),
                    (10 * 1024 * 1024).toLong()) // 10 MB
        } catch (e: Exception) {
            Log.e(TAG, "Could not create Cache!")
        }

        return cache
    }

    private fun initCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.DAYS)
                    .build()

            response.newBuilder()
                    .removeHeader(PRAGMA_HEADER)
                    .removeHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER)
                    .removeHeader(CACHE_CONTROL_HEADER)
                    .header(CACHE_CONTROL_HEADER, cacheControl.toString())
                    .build()
        }
    }

    private fun initOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()

            if (!NetworkUtils.isInternetAvailable(app)) {
                val cacheControl = CacheControl.Builder()
                        .maxStale(30, TimeUnit.DAYS)
                        .build()

                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()
            }

            chain.proceed(request)
        }
    }

    companion object {
        private const val TAG = "NewsModule"
        private const val CACHE_CONTROL_HEADER = "Cache-Control"
        private const val PRAGMA_HEADER = "Pragma"
        private const val ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin"
    }
}
