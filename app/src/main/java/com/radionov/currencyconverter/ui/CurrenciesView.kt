package com.radionov.currencyconverter.ui

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * @author Andrey Radionov
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface CurrenciesView : MvpView {

    fun showProgress()

    fun hideProgress()

    fun showResult(result: Double)

    fun showNetworkError()

    fun showNotFoundError()

    fun hideErrorDialog()
}