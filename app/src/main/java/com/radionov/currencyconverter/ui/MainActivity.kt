package com.radionov.currencyconverter.ui

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.radionov.currencyconverter.CurrencyApp
import com.radionov.currencyconverter.R
import com.radionov.currencyconverter.presenter.CurrencyPresenter
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), CurrenciesView {

    @Inject
    @InjectPresenter
    lateinit var currencyPresenter: CurrencyPresenter

    @ProvidePresenter
    fun providePresenter(): CurrencyPresenter {
        return currencyPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CurrencyApp.appComponent.inject(this)
    }
}
