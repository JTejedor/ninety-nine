package com.ninety.nine.main.restbackend.currencies

import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.GroupCount
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/currencies")
class CurrencyController(private val currencyService: CurrencyService) {

    @GetMapping("/")
    @ResponseBody
    fun get(): Flux<Group> {
        return currencyService.getReceivedDifferentCurrencies()
    }

    @GetMapping("/count")
    @ResponseBody
    fun count(): Flux<GroupCount> {
        return currencyService.getReceivedDifferentCurrenciesCount()
    }
}