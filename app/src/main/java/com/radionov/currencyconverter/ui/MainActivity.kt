package com.radionov.currencyconverter.ui

import android.os.Bundle
import android.support.v7.app.AlertDialog
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
import com.radionov.currencyconverter.utils.NetworkUtils

class MainActivity : MvpAppCompatActivity(), CurrenciesView {

    @Inject
    @InjectPresenter
    lateinit var currencyPresenter: CurrencyPresenter

    @ProvidePresenter
    fun providePresenter(): CurrencyPresenter {
        return currencyPresenter
    }

    private var errorDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        CurrencyApp.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSpinners()
        setupButtonListener()
    }

    override fun showProgress() {
        setViewsEnabled(false)
        btnConvert.text = EMPTY_STRING
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

    override fun showError() {
        if (NetworkUtils.isInternetAvailable(this)) {
            showErrorDialog(R.string.not_found_title, R.string.not_found_msg)
        } else {
            showErrorDialog(R.string.connection_error_title, R.string.connection_error_msg)
        }
        hideResult()
    }

    override fun hideErrorDialog() {
        errorDialog?.dismiss()
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
        tvResult.text = EMPTY_STRING
        val from = spinnerFromCurrency.selectedItem.toString()
        val to = spinnerToCurrency.selectedItem.toString()
        currencyPresenter.fetchCurrency("${from}_$to")
    }

    private fun setViewsEnabled(enabled: Boolean = true) {
        btnConvert.isEnabled = enabled
        spinnerFromCurrency.isEnabled = enabled
        spinnerToCurrency.isEnabled = enabled
    }

    private fun hideResult() {
        resultGroup.visibility = View.GONE
        tvResult.text = EMPTY_STRING
    }

    private fun showErrorDialog(errorTitleId: Int, errorMessageId: Int) {
        errorDialog = AlertDialog.Builder(this)
                .setTitle(getString(errorTitleId))
                .setMessage(getString(errorMessageId))
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    currencyPresenter.hideErrorDialog()
                }
                .show()
    }

    companion object {
        const val EMPTY_STRING = ""
        const val USD_ARRAY_POSITION = 8
        const val RUB_ARRAY_POSITION = 141
    }
}
