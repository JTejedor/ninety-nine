package com.ninety.nine.main.restbackend.transfer

import com.ninety.nine.main.restbackend.data.DayTransferGroupCount
import com.ninety.nine.main.restbackend.data.MonthTransferGroupCount
import com.ninety.nine.main.restbackend.data.YearTransferGroupCount
import org.bson.Document
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters.*

interface TransferCounter<T> {
    fun count(startDateTime: LocalDateTime): Flux<T>
    fun count(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Flux<T>
}

fun createAggregationOperation(map: Map<String, Document>): AggregationOperation {
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
    val matchStage: MatchOperation = Aggregation.match(criteria)
    val aggregation = newAggregation(
        matchStage,
        aggregationOperation
    )
    return reactiveMongoTemplate.aggregate(aggregation, "transfers", T::class.java)
}


interface AggregationComponentCreator {
    fun createCriteria(startDate: LocalDateTime): Criteria
    fun createCriteria(startDate: LocalDateTime, endDate: LocalDateTime): Criteria
    fun createIdentificationMap(): Map<String, Document>
}

class DayAggregationCreator : AggregationComponentCreator {

    override fun createCriteria(startDate: LocalDateTime): Criteria {
        val localDate: LocalDate = startDate.toLocalDate()
        val startMonthDayDateTime = localDate.with(firstDayOfMonth()).atStartOfDay()
        val endMontDayDateTime = localDate.with(lastDayOfMonth()).atStartOfDay().plusDays(1)
        return Criteria.where("timestamp")
            .gte(startMonthDayDateTime)
            .lt(endMontDayDateTime)
    }

    override fun createCriteria(startDate: LocalDateTime, endDate: LocalDateTime): Criteria {
        return Criteria.where("timestamp")
            .gte(startDate)
            .lt(endDate)
    }

    override fun createIdentificationMap(): Map<String, Document> {
        return mapOf(
            "year" to Document("\$year", "\$timestamp"),
            "month" to Document("\$month", "\$timestamp"),
            "day" to Document("\$dayOfMonth", "\$timestamp")
        )
    }
}

@Component
class DayTransferCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate) : TransferCounter<DayTransferGroupCount> {
    private val creator = DayAggregationCreator()
    override fun count(startDateTime: LocalDateTime): Flux<DayTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime),
            createAggregationOperation(creator.createIdentificationMap())
        )
    }

    override fun count(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Flux<DayTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime, endDateTime),
            createAggregationOperation(creator.createIdentificationMap())
        )
    }
}



open class MonthAggregationCreator : AggregationComponentCreator {

    override fun createCriteria(startDate: LocalDateTime): Criteria {
        val localDate: LocalDate = startDate.toLocalDate()
        val start: LocalDateTime = localDate.with(firstDayOfYear()).atStartOfDay()
        val end: LocalDateTime = localDate.with(lastDayOfYear()).atStartOfDay().plusDays(1)
        return Criteria.where("timestamp")
            .gte(start)
            .lt(end)
    }

    override fun createCriteria(startDate: LocalDateTime, endDate: LocalDateTime): Criteria {
        return Criteria.where("timestamp")
            .gte(startDate)
            .lt(endDate)
    }



    override fun createIdentificationMap(): Map<String, Document> {
        return mapOf(
            "year" to Document("\$year", "\$timestamp"),
            "month" to Document("\$month", "\$timestamp")
        )
    }
}

@Component
class MonthTransferCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate) :
    TransferCounter<MonthTransferGroupCount> {
    private val creator = MonthAggregationCreator()
    override fun count(startDateTime: LocalDateTime): Flux<MonthTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime),
            createAggregationOperation(creator.createIdentificationMap())
        )
    }

    override fun count(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Flux<MonthTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime, endDateTime),
            createAggregationOperation(creator.createIdentificationMap())
        )
    }
}

class YearAggregationCreator : AggregationComponentCreator {
    companion object{
        private const val YEAR_MAXIMUM_POSSIBLE_RANGE: Long = 5
    }
    override fun createCriteria(startDate: LocalDateTime): Criteria {
        val localDate: LocalDate = startDate.toLocalDate()
        val firstDayOfYear: LocalDate = localDate.with(firstDayOfYear()).minusYears(YEAR_MAXIMUM_POSSIBLE_RANGE)
        val endDayOfYear: LocalDate = localDate.with(lastDayOfYear()).plusDays(1)
        return Criteria.where("timestamp")
            .gte(firstDayOfYear.atStartOfDay())
            .lt(endDayOfYear.atStartOfDay())
    }

    override fun createCriteria(startDate: LocalDateTime, endDate: LocalDateTime): Criteria {
        return Criteria.where("timestamp")
            .gte(startDate)
            .lt(endDate)
    }

    override fun createIdentificationMap(): Map<String, Document> {
        return mapOf(
            "year" to Document("\$year", "\$timestamp"),
            "month" to Document("\$month", "\$timestamp")
        )
    }
}

@Component
class YearTransferTransferCounter(private val reactiveMongoTemplate: ReactiveMongoTemplate) : TransferCounter<YearTransferGroupCount> {
    private val creator = YearAggregationCreator()
    override fun count(startDateTime: LocalDateTime): Flux<YearTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime),
            createAggregationOperation(creator.createIdentificationMap())
        )
    }

    override fun count(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Flux<YearTransferGroupCount> {
        return createAggregation(
            reactiveMongoTemplate,
            creator.createCriteria(startDateTime,endDateTime),
            createAggregationOperation(creator.createIdentificationMap())
        )
    }
}

