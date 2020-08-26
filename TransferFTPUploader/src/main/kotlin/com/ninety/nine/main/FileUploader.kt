package com.ninety.nine.main

import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import java.util.concurrent.ThreadLocalRandom

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "file")
class FileConfiguration {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FileConfiguration::class.java)
    }

    var transferAmountLines: Long = 0

    fun printConfiguration() {
        logger.info("")
        logger.info("+++++++++++++++++++++++++++++++++++")
        logger.info("++++FILE UPLOADER CONFIGURATION++++")
        logger.info("TRANSFER AMOUNT LINES: $transferAmountLines")
        logger.info("+++++++++++++++++++++++++++++++++++")
    }
}

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ftp-server")
class FTPServerConfiguration {
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
class FileGenerator(
    private val fileConfiguration: FileConfiguration,
    private val transferGenerator: TransferGenerator
) {
    companion object {

        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FileGenerator::class.java)
    }

    fun create(name: String): String {
        val randomNumberOfLines =
            ThreadLocalRandom.current().nextLong(0, fileConfiguration.transferAmountLines)
        try {
            val file = File(name)
            file.bufferedWriter().use { out ->
                for (i in 0 until randomNumberOfLines) {
                    out.write(transferGenerator.generate())
                    out.write("\n")
                }
            }
            logger.info("CREATED FILE $name OF $randomNumberOfLines LINES")
            return file.absolutePath
        } catch (ex: Exception) {
            logger.error("ERROR CREATING FILE $name OF $randomNumberOfLines LINES")
            return ""
        }
    }
}

@Component
class CustomFtpClient(private val ftpServerConfiguration: FTPServerConfiguration) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(CustomFtpClient::class.java)
    }

    private val ftpClient: FTPClient = FTPClient()
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

    private fun getFileName(path: String): String {
        return path.substringAfterLast("/")
    }

    fun uploadFile(path: String): Boolean {
        val uploadFileStream = FileInputStream(path)
        val name = getFileName(path)
        return try {
            connect()
            ftpClient.storeFile(name, uploadFileStream)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            ftpClient.disconnect()
            uploadFileStream.close()
            File(path).delete()
            logger.info("UPLOAD FILE $name to server ${ftpServerConfiguration.hostname}")
        }
    }
}

@Component
class FileUploader(private val fileGenerator: FileGenerator, private val ftpClient: CustomFtpClient) {
    companion object {
        private val FTP_FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
        private const val MINUS_YEARS: Long = 5
        private const val MAXIMUM_FILES_PER_MONTH = 30
    }

    fun upload() {
        val now: LocalDateTime = LocalDateTime.now()
        var currentFirstDayMonth: LocalDateTime = now.with(firstDayOfMonth())
        val fiveYearInPastDate = currentFirstDayMonth.minusYears(MINUS_YEARS)
        while (currentFirstDayMonth > fiveYearInPastDate) {
            val endOfMonth = getEndOfMonth(currentFirstDayMonth)
            generateMonth(currentFirstDayMonth, endOfMonth)
            currentFirstDayMonth = currentFirstDayMonth.minusMonths(1)
        }
    }

    private fun getEndOfMonth(startOfMonth: LocalDateTime): LocalDateTime {
        val now = LocalDateTime.now()
        val endDateTime: LocalDateTime = startOfMonth.with(lastDayOfMonth()).plusDays(1)
        return if (endDateTime > now) now else endDateTime
    }

    private fun generateFileName(startOfMonth: LocalDateTime, endOfMonth: LocalDateTime): String {
        val start = startOfMonth.toEpochSecond(ZoneOffset.UTC)
        val end = endOfMonth.toEpochSecond(ZoneOffset.UTC)
        val randomTime =
            ThreadLocalRandom.current().nextLong(start, end)
        val usedDate = LocalDateTime.ofEpochSecond(randomTime, 0, ZoneOffset.UTC);
        return usedDate.format(FTP_FILE_DATE_FORMAT).plus(".csv")
    }

    private fun generateMonth(startOfMonth: LocalDateTime, endOfMonth: LocalDateTime) {
        val numberOfFilesThisMonth =
            ThreadLocalRandom.current().nextInt(1, MAXIMUM_FILES_PER_MONTH)
        for (x in 0..numberOfFilesThisMonth) {
            val fileName = generateFileName(startOfMonth, endOfMonth)
            val path = fileGenerator.create(fileName)
            if (!path.isBlank())
                ftpClient.uploadFile(path)
        }
    }
}