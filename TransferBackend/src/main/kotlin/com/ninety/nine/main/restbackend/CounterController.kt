package com.ninety.nine.main.restbackend

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


@RestController
@RequestMapping("/count")
class CounterController(private val yearCounter : Counter<YearTransferGroupCount>,
                      private val monthCounter : Counter<MonthTransferGroupCount>,
                      private val dayCounter : Counter<DayTransferGroupCount>) {

    @GetMapping("/year/{timestamp}")
    @ResponseBody
    fun yearCount(@PathVariable("timestamp") timestamp: Long): Flux<YearTransferGroupCount>{
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return yearCounter.count(localDateTime)
    }

    @GetMapping("/month/{timestamp}")
    @ResponseBody
    fun monthCount(@PathVariable("timestamp") timestamp: Long): Flux<MonthTransferGroupCount>{
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return monthCounter.count(localDateTime)
    }

    @GetMapping("/day/{timestamp}")
    @ResponseBody
    fun dayCount(@PathVariable("timestamp") timestamp: Long): Flux<DayTransferGroupCount>{
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dayCounter.count(localDateTime)
    }
}