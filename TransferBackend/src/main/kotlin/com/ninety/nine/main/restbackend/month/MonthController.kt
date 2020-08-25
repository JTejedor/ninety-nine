package com.ninety.nine.main.restbackend.month

import com.ninety.nine.main.restbackend.data.MonthTransferGroupMaxAmount
import com.ninety.nine.main.restbackend.data.MonthTransferGroupMaxCount
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/month")
class TransferCounterController(
    private val transferMonthCounterService: TransferMonthCounterService
) {

    @GetMapping("/transfer-count-hit")
    @ResponseBody
    fun monthWithMaxCount(
        @RequestParam("start") startDate: LocalDateTime,
        @RequestParam("end") endDate: Optional<LocalDateTime>
    ): Flux<MonthTransferGroupMaxCount> {
        return transferMonthCounterService.monthWithMaxCount(startDate, endDate.get())
    }

    @GetMapping("/transfer-amount-hit")
    @ResponseBody
    fun monthWithMaxAmount(
        @RequestParam("start") startDate: LocalDateTime,
        @RequestParam("end") endDate: Optional<LocalDateTime>
    ): Flux<MonthTransferGroupMaxAmount> {
        return transferMonthCounterService.monthWithMaxAmount(startDate, endDate.get())
    }
}