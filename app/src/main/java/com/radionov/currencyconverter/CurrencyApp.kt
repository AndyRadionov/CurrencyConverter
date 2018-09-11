package com.radionov.currencyconverter

import android.app.Application
import com.radionov.currencyconverter.di.AppComponent
import com.radionov.currencyconverter.di.DaggerAppComponent

/**
 * @author Andrey Radionov
 */
class CurrencyApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}