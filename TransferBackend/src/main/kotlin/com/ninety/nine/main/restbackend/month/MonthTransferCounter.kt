package com.ninety.nine.main.restbackend.month

import com.ninety.nine.main.restbackend.data.MonthTransferGroupMaxCount
import com.ninety.nine.main.restbackend.data.MonthTransferGroupMaxAmount
import com.ninety.nine.main.restbackend.transfer.*
import org.bson.Document
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.limit
import org.springframework.data.mongodb.core.aggregation.Aggregation.sort
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.aggregation.SortOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface SortOperationAggregationCreator : AggregationComponentCreator {
    fun createSort(): SortOperation
    fun createGroupDocument(): Document
}

class MonthMaxAmountAggregationCreator : MonthAggregationCreator(), SortOperationAggregationCreator {
    companion object {
        private const val MAX_AMOUNT_NAME = "maxAmount"
    }

    override fun createSort(): SortOperation {
        return sort(Sort.Direction.DESC, MAX_AMOUNT_NAME)
    }

    override fun createGroupDocument(): Document {
        val groupDocument = mapOf(
            "_id" to createIdentificationMap(),
            "maxAmount" to Document("\$sum", "\$amount")
        )
        return Document(
            "\$group",
            groupDocument
        )
    }
}

class MonthMaxTransferCountAggregationCreator : MonthAggregationCreator(), SortOperationAggregationCreator {
    companion object {
        private const val MAX_COUNT_NAME = "maxAmount"
    }

    override fun createSort(): SortOperation {
        return sort(Sort.Direction.DESC, MAX_COUNT_NAME)
    }

    override fun createGroupDocument(): Document {
        val groupDocument = mapOf(
            "_id" to createIdentificationMap(),
            "maxCount" to Document("\$sum", 1)
        )
        return Document(
            "\$group",
            groupDocument
        )
    }
}

fun createMonthAggregationOperation(groupDocument: Document): AggregationOperation {
    return AggregationOperation {
       groupDocument
    }
}

inline fun <reified T : Any> createMonthAggregation(
    reactiveMongoTemplate: ReactiveMongoTemplate,
    criteria: Criteria,
    aggregationOperation: AggregationOperation,
    sortOperation: SortOperation
): Flux<T> {
    val matchStage: MatchOperation = Aggregation.match(criteria);
    val aggregation = Aggregation.newAggregation(
        matchStage,
        aggregationOperation,
        sortOperation,
        limit(1)
    )
    return reactiveMongoTemplate.aggregate(aggregation, "transfers", T::class.java)
}

@Component
class MonthTransferMaxAmountCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate) :
    TransferCounter<MonthTransferGroupMaxAmount> {
    private val creator = MonthMaxAmountAggregationCreator()
    override fun count(startDateTime: LocalDateTime): Flux<MonthTransferGroupMaxAmount> {
        return createMonthAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime),
            createMonthAggregationOperation(creator.createGroupDocument()),
            creator.createSort()
        )
    }

    override fun count(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Flux<MonthTransferGroupMaxAmount> {
        return createMonthAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime, endDateTime),
            createMonthAggregationOperation(creator.createGroupDocument()),
            creator.createSort()
        )
    }
}

@Component
class MonthTransferMaxCountCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate) :
    TransferCounter<MonthTransferGroupMaxCount> {
    private val creator = MonthMaxTransferCountAggregationCreator()
    override fun count(startDateTime: LocalDateTime): Flux<MonthTransferGroupMaxCount> {
        return createMonthAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime),
            createMonthAggregationOperation(creator.createGroupDocument()),
            creator.createSort()
        )
    }

    override fun count(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Flux<MonthTransferGroupMaxCount> {
        return createMonthAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime, endDateTime),
            createMonthAggregationOperation(creator.createGroupDocument()),
            creator.createSort()
        )
    }
}

