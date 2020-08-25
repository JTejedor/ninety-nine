package com.ninety.nine.main.restbackend.transfer

import com.ninety.nine.main.restbackend.data.DayTransferGroupCount
import com.ninety.nine.main.restbackend.data.MonthTransferGroupCount
import com.ninety.nine.main.restbackend.data.YearTransferGroupCount
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.LocalDateTime

@Service
class TransferCounterService(
    private val yearCounter: TransferCounter<YearTransferGroupCount>,
    private val monthCounter: TransferCounter<MonthTransferGroupCount>,
    private val dayCounter: TransferCounter<DayTransferGroupCount>
) {
    private fun <T> count(counter: TransferCounter<T>, startDate: LocalDateTime,
                          endDate: LocalDateTime?): Flux<T>{
        return if (endDate != null)
            counter.count(startDate, endDate)
        else
            counter.count(startDate)
    }
    fun yearTransferCount(
        startDate: LocalDateTime,
        endDate: LocalDateTime?
    ): Flux<YearTransferGroupCount> {
        return count(yearCounter, startDate, endDate)
    }

    fun monthTransferCount(
        startDate: LocalDateTime,
        endDate: LocalDateTime?
    ): Flux<MonthTransferGroupCount> {
        return count(monthCounter, startDate, endDate)
    }

    fun dayTransferCount(
        startDate: LocalDateTime,
        endDate: LocalDateTime?
    ): Flux<DayTransferGroupCount> {
        return count(dayCounter, startDate, endDate)
    }
}