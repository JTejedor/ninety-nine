package com.ninety.nine.main.restbackend.currencies

import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.GroupCount
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.*

interface CurrencyDao {
    fun getReceivedDifferentCurrencies(): Flux<Group>
    fun getReceivedDifferentCurrenciesCount(): Flux<GroupCount>
}

@Component
class CurrencyDaoImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate) : CurrencyDao {
    override fun getReceivedDifferentCurrencies(): Flux<Group> {
        val groupAggregation = group("currency")
        val aggregation = newAggregation(groupAggregation)
        return reactiveMongoTemplate.aggregate(aggregation, "transfers", Group::class.java)
    }

    override fun getReceivedDifferentCurrenciesCount(): Flux<GroupCount> {
        val groupAggregation = group("currency").count().`as`("count")
        val aggregation = newAggregation(groupAggregation)
        return reactiveMongoTemplate.aggregate(aggregation, "transfers", GroupCount::class.java)
    }

}
