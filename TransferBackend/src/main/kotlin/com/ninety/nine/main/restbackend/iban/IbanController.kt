package com.ninety.nine.main.restbackend.iban

import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.Transfer
import org.iban4j.Iban
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
class IbanEndpointController(
    private val ibanService: IbanService
) {

    @GetMapping("/iban")
    @ResponseBody
    fun get(): Flux<Group> {
        return ibanService.get()
    }

    @GetMapping("iban/transfers/{iban}")
    @ResponseBody
    fun getLastTransfersByIban(
        @PathVariable iban: Iban,
        @RequestParam("start") start: Long,
        @RequestParam("limit") limit: Int
    ): Flux<Transfer> {
        return ibanService.getLatestTransfersByIban(iban, start, limit)
    }
}