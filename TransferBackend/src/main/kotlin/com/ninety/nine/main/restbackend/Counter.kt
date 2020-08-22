package com.ninety.nine.main.restbackend

import org.bson.Document
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.limit
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

interface Counter<T>
{
    fun count(time: LocalDateTime):Flux<T>
}

fun createAggregationOperation(map: Map<String, Any>): AggregationOperation {
    return AggregationOperation {
        val idDocument = Document(map)
        val groupDocument = Document(
            mapOf(
                "_id" to idDocument,
                "count" to Document("\$sum", 1)
            )
        )
        Document(
            "\$group",
            groupDocument
        )
    }
}


inline fun <reified T : Any> createAggregation(
    reactiveMongoTemplate: ReactiveMongoTemplate,
    criteria: Criteria,
    aggregationOperation: AggregationOperation
): Flux<T> {
    val matchStage: MatchOperation = Aggregation.match(criteria);
    val aggregation = Aggregation.newAggregation(
        matchStage,
        aggregationOperation,
        limit(1)
    )
    return reactiveMongoTemplate.aggregate(aggregation, "transfers", T::class.java)
}


interface AggregationComponentCreator {
    fun createCriteria(time: LocalDateTime): Criteria
    fun createMap(): Map<String, Any>
}

data class DayAggregationKey(
    val year: Int,
    val month: Int,
    val day: Int
)

data class DayTransferGroupCount(
    @Id
    val id: DayAggregationKey,
    val count: Int
)

class DayAggregationCreator : AggregationComponentCreator {

    override fun createCriteria(time: LocalDateTime): Criteria {
        val localDate: LocalDate = time.toLocalDate()
        return Criteria.where("timestamp")
            .gte(localDate.atStartOfDay())
            .lt(localDate.plusDays(1).atStartOfDay())
    }

    override fun createMap(): Map<String, Any> {
        return mapOf(
            "year" to Document("\$year", "\$timestamp"),
            "month" to Document("\$month", "\$timestamp"),
            "day" to Document("\$dayOfMonth", "\$timestamp")
        )
    }
}

@Component
class DayTransferCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate): Counter<DayTransferGroupCount> {
    private val creator = DayAggregationCreator()
    override fun count(time: LocalDateTime): Flux<DayTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(time),
            createAggregationOperation(creator.createMap())
        )
    }
}

data class MonthAggregationKey(
    val year: Int,
    val month: Int
)

data class MonthTransferGroupCount(
    @Id
    val id: MonthAggregationKey,
    val count: Int
)

class MonthAggregationCreator : AggregationComponentCreator {

    override fun createCriteria(time: LocalDateTime): Criteria {
        val localDate: LocalDate = time.toLocalDate()
        val start: LocalDate = localDate.withDayOfMonth(1)
        val end: LocalDate = localDate.withDayOfMonth(localDate.lengthOfMonth()).plusDays(1)
        return Criteria.where("timestamp")
            .gte(start.atStartOfDay())
            .lt(end.atStartOfDay())
    }

    override fun createMap(): Map<String, Any> {
        return mapOf(
            "year" to Document("\$year", "\$timestamp"),
            "month" to Document("\$month", "\$timestamp")
        )
    }
}

@Component
class MonthTransferCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate): Counter<MonthTransferGroupCount> {
    private val creator = MonthAggregationCreator()
    override fun count(time: LocalDateTime): Flux<MonthTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(time),
            createAggregationOperation(creator.createMap())
        )
    }
}

data class YearAggregationKey(
    val year: Int
)

data class YearTransferGroupCount(
    @Id
    val id: YearAggregationKey,
    val count: Int
)

class YearAggregationCreator:AggregationComponentCreator{

    override fun createCriteria(time: LocalDateTime): Criteria {
        val localDate: LocalDate = time.toLocalDate()
        val firstDayOfYear: LocalDate = localDate.with(TemporalAdjusters.firstDayOfYear())
        val endDayOfYear: LocalDate = localDate.with(TemporalAdjusters.lastDayOfYear()).plusDays(1)
        println(time)
        println(firstDayOfYear.toString())
        println(endDayOfYear.toString())
        return  Criteria.where("timestamp")
            .gte(firstDayOfYear.atStartOfDay())
            .lt(endDayOfYear.atStartOfDay())
    }

    override fun createMap(): Map<String, Any> {
        return mapOf(
            "year" to Document("\$year", "\$timestamp"),
            "month" to Document("\$month", "\$timestamp")
        )
    }
}

@Component
class YearTransferCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate): Counter<YearTransferGroupCount> {
    private val creator = YearAggregationCreator()
    override fun count(time: LocalDateTime): Flux<YearTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(time),
            createAggregationOperation(creator.createMap())
        )
    }
}

