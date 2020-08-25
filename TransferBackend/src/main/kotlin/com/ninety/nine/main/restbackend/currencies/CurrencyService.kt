package com.ninety.nine.main.restbackend.currencies

import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.GroupCount
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class CurrencyService(private val currencyDao: CurrencyDao) {
    fun getReceivedDifferentCurrencies(): Flux<Group> {
        return currencyDao.getReceivedDifferentCurrencies()
    }

    fun getReceivedDifferentCurrenciesCount(): Flux<GroupCount> {
        return currencyDao.getReceivedDifferentCurrenciesCount()
    }
}