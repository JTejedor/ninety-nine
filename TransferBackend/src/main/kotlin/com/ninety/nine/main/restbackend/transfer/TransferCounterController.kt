package com.ninety.nine.main.restbackend.transfer

import com.ninety.nine.main.restbackend.data.DayTransferGroupCount
import com.ninety.nine.main.restbackend.data.MonthTransferGroupCount
import com.ninety.nine.main.restbackend.data.YearTransferGroupCount
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.lang.NumberFormatException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@Component
class StringToLongTimestamp : Converter<String, LocalDateTime> {
    override fun convert(source: String): LocalDateTime? {
        if (source.length != 10 && source.length != 13) {
            throw NumberFormatException("source is not a valid number")
        }

        val timestamp: Long = source.toLong()
        return if (source.length == 10)//seconds{
            LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
        else
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"))
    }
}


@RestController
@RequestMapping("/count")
class TransferCounterController(
    private val transferCounterService: TransferCounterService
) {

    @GetMapping("/year")
    @ResponseBody
    fun yearCount(
        @RequestParam("start") startDate: LocalDateTime,
        @RequestParam("end") endDate: Optional<LocalDateTime>
    ): Flux<YearTransferGroupCount> {
        return transferCounterService.yearTransferCount(startDate, endDate.get())
    }

    @GetMapping("/month")
    @ResponseBody
    fun monthCount(
        @RequestParam("start") startDate: LocalDateTime,
        @RequestParam("end") endDate: Optional<LocalDateTime>
    ): Flux<MonthTransferGroupCount> {
        return transferCounterService.monthTransferCount(startDate, endDate.get())
    }

    @GetMapping("/day")
    @ResponseBody
    fun dayCount(
        @RequestParam("start") startDate: LocalDateTime,
        @RequestParam("end") endDate: Optional<LocalDateTime>
    ): Flux<DayTransferGroupCount> {
        return transferCounterService.dayTransferCount(startDate, endDate.get())
    }
}