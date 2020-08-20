package com.ninety.nine.main.mongouploader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

interface Saver<T> {
    fun save(fileExtractionResult: FileExtractionResult)
}

@Component
final class TransferSaver(private val mongoTemplate: MongoTemplate) : Saver<Transfer> {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(TransferSaver::class.java)
    }
    override fun save(fileExtractionResult: FileExtractionResult) {
        if (fileExtractionResult.transfers.size == 0)
            return
        logger.info("Saving transfer. count ${fileExtractionResult.transfers.size}")
        val savedElements: Collection<Transfer> = mongoTemplate.insertAll(fileExtractionResult.transfers)
        logger.info("Saved transfer. count ${savedElements.size}")
    }
}

@Component
final class FileStatisticsSaver(private val mongoTemplate: MongoTemplate) : Saver<FileStatistics> {
    override fun save(fileExtractionResult: FileExtractionResult) {
        val statistics = FileStatistics(
            null,
            fileExtractionResult.filename,
            fileExtractionResult.successRatio,
            fileExtractionResult.successfulTransfers,
            fileExtractionResult.failedTransfers
        )
        mongoTemplate.save(statistics)
    }
}


interface ChangedFileGlobalStatisticsSubscriber {
    fun update(fileName: String)
}

interface ChangedFileGlobalStatisticsObservable{
    fun provideLastProcessedFile(): String
    fun subscribe(subscriber: ChangedFileGlobalStatisticsSubscriber)
    fun unsubscribe(subscriber: ChangedFileGlobalStatisticsSubscriber)
}

interface GlobalStatisticsProvider {
    fun update(fileExtractionResult: FileExtractionResult)
}

@Component
final class GlobalStatisticsManager(private val mongoTemplate: MongoTemplate) : GlobalStatisticsProvider, ChangedFileGlobalStatisticsObservable {
    private val statistics: GlobalStatistics
    private val subscribers = ArrayList<ChangedFileGlobalStatisticsSubscriber>()

    init {
        val query = Query()
        statistics = mongoTemplate.findOne(query, GlobalStatistics::class.java) ?: GlobalStatistics.emptyStatistics()
    }

    override fun provideLastProcessedFile(): String {
        return statistics.lastProcessedFileName
    }

    override fun subscribe(subscriber: ChangedFileGlobalStatisticsSubscriber) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: ChangedFileGlobalStatisticsSubscriber) {
        subscribers.remove(subscriber)
    }

    override fun update(fileExtractionResult: FileExtractionResult) {
        updateStatistics(fileExtractionResult)
        mongoTemplate.save(statistics)
        notifyChangedFile()
    }

    private fun notifyChangedFile() {
        for (subscriber in subscribers) {
            subscriber.update(statistics.lastProcessedFileName)
        }
    }

    private fun updateStatistics(fileExtractionResult: FileExtractionResult) {
        statistics.lastProcessedFileName = fileExtractionResult.filename
        statistics.globalFailedTransfers += fileExtractionResult.failedTransfers
        statistics.globalSuccessTransfers += fileExtractionResult.successfulTransfers
        statistics.globalSuccessRatio = calculateSuccessRatio()
    }

    private fun calculateSuccessRatio(): Double {
        val ratioDiv = (statistics.globalSuccessTransfers + statistics.globalFailedTransfers).toDouble()
        return statistics.globalSuccessTransfers.toDouble() / ratioDiv
    }
}

interface ResultDispatcher{
    fun dispatch(fileExtractionResult: FileExtractionResult)
}

@Component
final class FileExtractorResultDispatcher(
    private val transferSaver: Saver<Transfer>,
    private val fileSaver: Saver<FileStatistics>,
    private val globalStatisticsProvider: GlobalStatisticsProvider
): ResultDispatcher {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FileExtractorResultDispatcher::class.java)
    }
    override fun dispatch(fileExtractionResult: FileExtractionResult){
      try{
          logger.error("Saving results")
          transferSaver.save(fileExtractionResult)
          fileSaver.save(fileExtractionResult)
          globalStatisticsProvider.update(fileExtractionResult)
      }
      catch (ex:Exception){
          logger.error(convertStactTrace(ex))
      }
    }
}

