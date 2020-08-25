package com.ninety.nine.main.restbackend.data

import org.bson.types.ObjectId
import org.iban4j.Iban
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "transfers")
data class Transfer(
    @Id
    var id: ObjectId?,
    var timestamp: LocalDateTime,
    var iban: Iban,
    var nif: String,
    var currency: Currency,
    var amount: Double
) {
    override fun toString(): String {
        return "Transfer(id=$id, timestamp=$timestamp, iban=$iban, nif='$nif', currency=$currency, amount=$amount)"
    }
}

@Document(collection = "globalstatistics")
data class GlobalStatistics(
    @Id
    var id: ObjectId?,
    var lastProcessedFileName: String,
    var globalSuccessRatio: Double,
    var globalSuccessTransfers: Long,
    var globalFailedTransfers: Long
) {
    companion object {
        fun emptyStatistics(): GlobalStatistics {
            return GlobalStatistics(null, "", 0.00, 0, 0)
        }
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

data class MonthTransferGroupMaxAmount(
    @Id
    val id: MonthAggregationKey,
    val maxAmount: Double
)

data class MonthTransferGroupMaxCount(
    @Id
    val id: MonthAggregationKey,
    val maxCount: Int
)

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

data class YearAggregationKey(
    val year: Int
)

data class YearTransferGroupCount(
    @Id
    val id: YearAggregationKey,
    val count: Int
)

data class GroupCount(
    @Id
    val id: String,
    val count: Long
)

data class Group(
    @Id
    val id: String
)
