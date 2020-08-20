package com.ninety.nine.main.mongouploader

import org.apache.commons.net.ftp.*
import org.apache.commons.net.ftp.parser.DefaultFTPFileEntryParserFactory
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory
import org.apache.commons.net.ftp.parser.UnixFTPEntryParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Component
@ConfigurationProperties(prefix = "ftp-server")
final class FTPServerConfiguration {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FTPServerConfiguration::class.java)
    }

    var hostname = "localhost"
    var user = "ninetynine"
    var password = "ninetynine-rules"

    fun printConfiguration() {
        logger.info("")
        logger.info("+++++++++++++++++++++++++++++++++++")
        logger.info("++++SERVER UPLOADER CONFIGURATION++++")
        logger.info("FTP USER: $user")
        logger.info("FTP PASSWORD: $password")
        logger.info("FTP HOSTNAME: $hostname")
        logger.info("+++++++++++++++++++++++++++++++++++")
    }
}

@Component
final class CustomDateTimeFormatter {
    companion object {
        private const val DEFAULT_DATE = "19700101T000000Z"
        private val FTP_FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
    }

    fun format(dateTime: String): LocalDateTime {
        return if (dateTime.isBlank())
            LocalDateTime.parse(DEFAULT_DATE, FTP_FILE_DATE_FORMAT)
        else {
            val noExtensionFileNAme = if (dateTime.endsWith("csv")) dateTime.split(".")[0] else dateTime
            LocalDateTime.parse(noExtensionFileNAme, FTP_FILE_DATE_FORMAT)
        }
    }
}

class CustomFTPFileEntryParser(
    lastProcessedFileName: String = ""
) : UnixFTPEntryParser() {
    companion object {

        private val REGEX = Regex("\\d{8}T\\d{6}Z\\.csv") //yyyyMMdd'T'HHmmss'Z'
    }
    private val customDateTimeFormatter = CustomDateTimeFormatter()
    private val lastDateTime = customDateTimeFormatter.format(lastProcessedFileName)


    private fun isASuitableFile(name: String): Boolean {
        return name.matches(REGEX) && isAlreadyProcessedFile(name)
    }

    private fun isAlreadyProcessedFile(name: String): Boolean {
        val fileLocalDateTime = customDateTimeFormatter.format(name)
        return fileLocalDateTime > lastDateTime
    }

    override fun parseFTPEntry(listEntry: String?): FTPFile? {
        val file = super.parseFTPEntry(listEntry)
        val name = file.name
        if (isASuitableFile(name)) {
            return file
        }
        return null
    }
}

@Component
final class CustomFtpFileEntryParseFactory(globalStatisticsObservable: ChangedFileGlobalStatisticsObservable) :
    FTPFileEntryParserFactory, ChangedFileGlobalStatisticsSubscriber {
    companion object {
        const val NINETY_NINE_KEY = "ninety-nine"
    }

    init {
        globalStatisticsObservable.subscribe(this)
    }

    private val defaultFactory: DefaultFTPFileEntryParserFactory = DefaultFTPFileEntryParserFactory()
    var lastProcessedFile: String = globalStatisticsObservable.provideLastProcessedFile()

    override fun createFileEntryParser(key: String?): FTPFileEntryParser {
        if (key == NINETY_NINE_KEY) {
            return CustomFTPFileEntryParser(lastProcessedFile)
        }
        return defaultFactory.createFileEntryParser(key)
    }

    override fun createFileEntryParser(config: FTPClientConfig?): FTPFileEntryParser {
        return defaultFactory.createFileEntryParser(config)
    }

    override fun update(fileName: String) {
        lastProcessedFile = fileName
    }
}

@Component
final class FTPReader(
    private val ftpServerConfiguration: FTPServerConfiguration,
    ftpFileEntryParserFactory: CustomFtpFileEntryParseFactory,
    private val fileExtractor: FileExtractor,
    private val resultDispatcher: FileExtractorResultDispatcher
) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FTPReader::class.java)
    }

    private val ftpClient: FTPClient = FTPClient()

    init {
        ftpClient.setParserFactory(ftpFileEntryParserFactory)
    }

    private fun connect() {
        //ftpClient.addProtocolCommandListener(PrintCommandListener(PrintWriter(System.out)))
        ftpClient.connect(ftpServerConfiguration.hostname)
        if (FTPReply.isNegativePermanent(ftpClient.replyCode)) {
            ftpClient.disconnect()
            throw Exception("Not possible to connect")
        }
        ftpClient.login(ftpServerConfiguration.user, ftpServerConfiguration.password)
        ftpClient.enterLocalPassiveMode()
    }

    private fun getFTPParseEngine(): FTPListParseEngine {
        return ftpClient.initiateListParsing(CustomFtpFileEntryParseFactory.NINETY_NINE_KEY, "")
    }

    private fun getNameFile(file: FTPFile): String {
        return file.name.split(".")[0]
    }

    private fun extract(file: FTPFile?) {
        if (file == null) {
            return
        }
        logger.info("Start extracting ${file.name}")
        val ftpFullFilePath = getFtpFullPath(file)
        val inputStream = ftpClient.retrieveFileStream(ftpFullFilePath)
        val result = fileExtractor.extract(getNameFile(file), inputStream)
        val success = ftpClient.completePendingCommand()
        if (success) {
            resultDispatcher.dispatch(result)
        } else {
            logger.error("File is no completed ")
        }
    }

    fun read() {
        try {
            connect()
            ftpClient.setFileType(FTP.ASCII_FILE_TYPE)
            val parser = getFTPParseEngine()
            while (parser.hasNext()) {
                val files = parser.getNext(25)
                for (file in files) {
                    extract(file)
                }
            }
        } catch (ex: Exception) {
            logger.error(convertStactTrace(ex))
        } finally {
            try {
                ftpClient.logout()
                ftpClient.disconnect()
            } catch (ex: Exception) {
                logger.error("Error closing ftp connection")
                logger.error(convertStactTrace(ex))
            }
        }
    }

    private fun getFtpFullPath(file: FTPFile): String {
        var workingDirectory: String = ftpClient.printWorkingDirectory()
        if (workingDirectory.last() != '/')
            workingDirectory += '/'
        return workingDirectory + file.name
    }
}