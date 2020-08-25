package com.ninety.nine.main.restbackend.currencies

import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.GroupCount
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
class CurrencyEndpointController(private val currencyService: CurrencyService) {

    @GetMapping("/currencies")
    @ResponseBody
    fun get(): Flux<Group> {
        return currencyService.getReceivedDifferentCurrencies()
    }

    @GetMapping("/currencies/count")
    @ResponseBody
    fun count(): Flux<GroupCount> {
        return currencyService.getReceivedDifferentCurrenciesCount()
    }
}