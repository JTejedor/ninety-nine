package com.ninety.nine.main

import org.apache.commons.net.PrintCommandListener
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="file")
class FileConfiguration {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FileConfiguration::class.java)
    }

    var transferAmountLines: Long = 0

    fun printConfiguration(){
        logger.info("")
        logger.info("+++++++++++++++++++++++++++++++++++")
        logger.info("++++FILE UPLOADER CONFIGURATION++++")
        logger.info("TRANSFER AMOUNT LINES: $transferAmountLines")
        logger.info("+++++++++++++++++++++++++++++++++++")
    }
}

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="ftp-server")
class FTPServerConfiguration {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FTPServerConfiguration::class.java)
    }
    var hostname = "localhost"
    var user = "ninetynine"
    var password = "ninetynine-rules"

    fun printConfiguration(){
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
        private val FTP_FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(FileGenerator::class.java)
    }

    private fun makeFileName(): String {
        val current = LocalDateTime.now()
        val localDate: LocalDate = current.toLocalDate()
        val fiveYearInPastSinceEpoch =localDate.minusYears(5).atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        val currentSinceEpoch = current.toEpochSecond(ZoneOffset.UTC)
        val randomTime =
            ThreadLocalRandom.current().nextLong(fiveYearInPastSinceEpoch, currentSinceEpoch)
        val usedDate = LocalDateTime.ofEpochSecond(randomTime, 0, ZoneOffset.UTC);
        return usedDate.format(FTP_FILE_DATE_FORMAT).plus(".csv")
    }

    fun create(): String {
        val randomNumberOfLines =
            ThreadLocalRandom.current().nextLong(0, fileConfiguration.transferAmountLines)
        val name = makeFileName()
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
        }
        catch (ex: Exception){
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
    fun uploadFile() {
        val path = fileGenerator.create()
        if(!path.isBlank())
            ftpClient.uploadFile(path)
    }
}