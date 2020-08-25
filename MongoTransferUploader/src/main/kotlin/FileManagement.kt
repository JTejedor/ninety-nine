package com.ninety.nine.main.mongouploader

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStream

interface FileExtractor {
    fun extract(fileName: String, inputStream: InputStream): FileExtractionResult
}

@Component
final class FileExtractorReader(private val transferParser: TransferParser) :
    FileExtractor {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FileExtractorReader::class.java)
    }
    override fun extract(fileName: String, inputStream: InputStream): FileExtractionResult {
        val inputReader = inputStream.reader()
        val bufferedReader = BufferedReader(inputReader)
        val transfers = ArrayList<Transfer>()
        var successCount: Long = 0
        var failCount: Long = 0
        bufferedReader.use { reader ->
            var line = reader.readLine()
            while (line != null) {
                try {
                    val transfer = transferParser.parse(fileName, line)
                    transfers.add(transfer)
                    ++successCount
                } catch (ex: Exception) {
                    ++failCount
                }
                line = reader.readLine()
            }
        }
        val successfulRatio: Double = successCount.toDouble() / (successCount + failCount).toDouble()
        logger.info("Extracted file $fileName success transfers $successCount failed transfers $failCount conversion ratio $successfulRatio")
        return FileExtractionResult(
            fileName,
            successfulRatio,
            successCount,
            failCount,
            transfers
        )
    }
}




