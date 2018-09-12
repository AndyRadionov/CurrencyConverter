package com.radionov.currencyconverter.ui

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.radionov.currencyconverter.CurrencyApp
import com.radionov.currencyconverter.R
import com.radionov.currencyconverter.presenter.CurrencyPresenter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.widget.ArrayAdapter

class MainActivity : MvpAppCompatActivity(), CurrenciesView {

    @Inject
    @InjectPresenter
    lateinit var currencyPresenter: CurrencyPresenter

    @ProvidePresenter
    fun providePresenter(): CurrencyPresenter {
        return currencyPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        CurrencyApp.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSpinners()
        setupButtonListener()
    }

    override fun showProgress() {
        setViewsEnabled(false)
        btnConvert.text = ""
        pbConvert.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        setViewsEnabled()
        btnConvert.text = getString(R.string.convert)
        pbConvert.visibility = View.INVISIBLE
    }

    override fun showResult(result: Double) {
        resultGroup.visibility = View.VISIBLE
        tvResult.text = result.toString()
    }

    override fun showNetworkError() {

    }

    override fun showServerError() {

    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_names, R.layout.item_spinner)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerFromCurrency.adapter = adapter
        spinnerToCurrency.adapter = adapter

        spinnerFromCurrency.setSelection(USD_ARRAY_POSITION)
        spinnerToCurrency.setSelection(RUB_ARRAY_POSITION)
    }

    private fun setupButtonListener() {
        btnConvert.setOnClickListener {
            showProgress()
            performRequest()
        }
    }

    private fun performRequest() {
        tvResult.text = ""
        val from = spinnerFromCurrency.selectedItem.toString()
        val to = spinnerToCurrency.selectedItem.toString()
        currencyPresenter.fetchCurrency("${from}_$to")
    }

    private fun setViewsEnabled(enabled: Boolean = true) {
        btnConvert.isEnabled = enabled
        spinnerFromCurrency.isEnabled = enabled
        spinnerToCurrency.isEnabled = enabled
    }

    companion object {
        const val USD_ARRAY_POSITION = 8
        const val RUB_ARRAY_POSITION = 141
    }
}
