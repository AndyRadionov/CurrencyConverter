package com.radionov.currencyconverter.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.radionov.currencyconverter.data.repositories.CurrencyRepository
import com.radionov.currencyconverter.ui.CurrenciesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Andrey Radionov
 */
@InjectViewState
class CurrencyPresenter @Inject constructor(private val currencyRepository: CurrencyRepository)
    : MvpPresenter<CurrenciesView>() {

    private var subscription: Disposable? = null

    fun fetchCurrency(query: String) {
        subscription?.dispose()
        viewState.showProgress()

        subscription = currencyRepository.fetchCurrency(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    viewState.hideProgress()
                    viewState.showResult(result.values.first())
                }, {
                    viewState.hideProgress()
                    viewState.showNetworkError()
                })
    }

    fun hideErrorDialog() {
        viewState.hideErrorDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.dispose()
    }
}