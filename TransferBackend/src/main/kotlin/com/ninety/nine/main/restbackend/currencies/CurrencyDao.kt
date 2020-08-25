package com.ninety.nine.main.restbackend.currencies

import com.ninety.nine.main.restbackend.data.CurrencyCountResult
import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.GroupCount
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CurrencyDao {
    fun getReceivedDifferentCurrencies(): Flux<Group>
    fun getTransferCountByCurrency(): Flux<GroupCount>
    fun getReceivedDifferentCurrenciesCount(): Mono<CurrencyCountResult>
}

@Component
class CurrencyDaoImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate) : CurrencyDao {
    override fun getReceivedDifferentCurrencies(): Flux<Group> {
        val groupAggregation = group("currency")
        val aggregation = newAggregation(groupAggregation)
        return reactiveMongoTemplate.aggregate(aggregation, "transfers", Group::class.java)
    }

    override fun getTransferCountByCurrency(): Flux<GroupCount> {
        val groupAggregation = group("currency").count().`as`("count")
        val aggregation = newAggregation(groupAggregation)
        return reactiveMongoTemplate.aggregate(aggregation, "transfers", GroupCount::class.java)
    }

    override fun getReceivedDifferentCurrenciesCount(): Mono<CurrencyCountResult> {
        val groupAggregation = group("currency")
        val countAggregation = group().count().`as`("currencies")
        val aggregation = newAggregation(groupAggregation, countAggregation)
        val flux = reactiveMongoTemplate.aggregate(aggregation, "transfers", CurrencyCountResult::class.java)
        return flux.next()
    }

}
