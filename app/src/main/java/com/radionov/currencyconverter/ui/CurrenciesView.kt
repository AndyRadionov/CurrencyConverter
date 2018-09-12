package com.radionov.currencyconverter.ui

import com.arellomobile.mvp.MvpView

/**
 * @author Andrey Radionov
 */
interface CurrenciesView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun showResult(result: Double)

    fun showNetworkError()

    fun showServerError()
}