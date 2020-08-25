package com.ninety.nine.main.mongouploader

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
    var iban: String,
    var nif: String,
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
){
    companion object{
        fun emptyStatistics(): GlobalStatistics{
            return GlobalStatistics(null,"",0.00,0,0)
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