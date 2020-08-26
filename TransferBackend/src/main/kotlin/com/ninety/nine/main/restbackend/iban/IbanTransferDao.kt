package com.ninety.nine.main.restbackend.iban

import com.ninety.nine.main.restbackend.data.Transfer
import org.iban4j.Iban
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

interface IbanTransferDao {
    fun getLatestTransfersByIban(iban: Iban, start: Long, limit: Int): Flux<Transfer>
}

@Component
class IbanTransferDaoImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate) : IbanTransferDao {
    override fun getLatestTransfersByIban(iban: Iban, start: Long, limit: Int): Flux<Transfer> {
        val criteria = Criteria.where("iban").`is`(iban.toString())
        val query = Query(criteria)
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
        return reactiveMongoTemplate.find(query.skip(start).limit(limit), Transfer::class.java)
    }

}