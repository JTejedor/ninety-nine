package com.ninety.nine.main.restbackend.iban

import com.ninety.nine.main.restbackend.data.Group
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

interface IbanDao {
    fun get(): Flux<Group>
}

@Component
class IbanDaoImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate): IbanDao{
    override fun get(): Flux<Group> {
        val groupAggregation = Aggregation.group("iban")
        val aggregation = Aggregation.newAggregation(groupAggregation)
        return reactiveMongoTemplate.aggregate(aggregation, "transfers", Group::class.java)
    }

}