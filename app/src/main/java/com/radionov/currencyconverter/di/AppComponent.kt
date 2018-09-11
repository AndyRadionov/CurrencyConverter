package com.radionov.currencyconverter.di

import com.radionov.currencyconverter.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Andrey Radionov
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}