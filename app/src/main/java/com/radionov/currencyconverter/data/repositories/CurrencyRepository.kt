package com.radionov.currencyconverter.data.repositories

import com.radionov.currencyconverter.data.network.CurrencyApi

/**
 * @author Andrey Radionov
 */
class CurrencyRepository (private val currencyApi: CurrencyApi) {

    fun fetchCurrency(query: String) = currencyApi.fetchCurrency(query)
}