package com.ninety.nine.main.restbackend.month

import com.ninety.nine.main.restbackend.data.*
import com.ninety.nine.main.restbackend.transfer.TransferCounter
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class TransferMonthCounterService(
    private val maxAmountCounter: TransferCounter<MonthTransferGroupMaxAmount>,
    private val maxCountCounter: TransferCounter<MonthTransferGroupMaxCount>
) {
    private fun <T> count(counter: TransferCounter<T>, startDate: LocalDateTime,
                          endDate: LocalDateTime?): Flux<T> {
        return if (endDate != null)
            counter.count(startDate, endDate)
        else
            counter.count(startDate)
    }
    fun monthWithMaxAmount(
        startDate: LocalDateTime,
        endDate: LocalDateTime?
    ): Mono<MonthTransferGroupMaxAmount> {
        return count(maxAmountCounter, startDate, endDate).elementAt(0)
    }

    fun monthWithMaxCount(
        startDate: LocalDateTime,
        endDate: LocalDateTime?
    ): Mono<MonthTransferGroupMaxCount> {
        return count(maxCountCounter, startDate, endDate).elementAt(0)
    }
}