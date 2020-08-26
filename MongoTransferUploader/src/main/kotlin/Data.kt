package com.ninety.nine.main.mongouploader

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "transfers")
@CompoundIndexes(value = [
    CompoundIndex(name = "iban_timestamp", def = "{'iban' : -1, 'timestamp': -1}"),
    CompoundIndex(name = "currency_timestamp", def = "{'currency' : -1, 'timestamp': -1}")
])
data class Transfer(
    @Id
    var id: ObjectId?,
    @Indexed
    var timestamp: LocalDateTime,
    @Indexed
    var iban: String,
    var nif: String,
    @Indexed
    var currency: String,
    var amount: Double,
    var eurConversion: Double
)

@Document(collection = "filestatistics")
data class FileStatistics(
    @Id
    var id: ObjectId?,
    var fileName: String,
    var successRatio: Double,
    var successfulTransfers: Long,
    var failedTransfers: Long
)

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

data class FileExtractionResult(
    val filename: String,
    val successRatio: Double,
    val successfulTransfers: Long,
    val failedTransfers: Long,
    val transfers: ArrayList<Transfer>
) {}