package com.ninety.nine.main.restbackend.currencies

import com.ninety.nine.main.restbackend.data.CurrencyCountResult
import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.GroupCount
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
class CurrencyEndpointController(private val currencyService: CurrencyService) {

    @GetMapping("/currencies")
    @ResponseBody
    fun get(): Flux<Group> {
        return currencyService.getReceivedDifferentCurrencies()
    }

    @GetMapping("/currencies/count")
    @ResponseBody
    fun count(): Mono<CurrencyCountResult> {
        return currencyService.getReceivedDifferentCurrenciesCount()
    }

    @GetMapping("/currencies/transfers")
    @ResponseBody
    fun getTransferCountByCurrency(): Flux<GroupCount> {
        return currencyService.getTransferCountByCurrency()
    }
}