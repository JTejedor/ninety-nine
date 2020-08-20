package com.ninety.nine.main.mongouploader

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PeriodicUploadTask(
    private val reader: FTPReader,
    ftpServerConfiguration: FTPServerConfiguration
) {
    companion object {
        const val UPLOAD_TIMEOUT: Long = 1000
    }

    init {
        ftpServerConfiguration.printConfiguration()
    }

    @Scheduled(fixedRate = UPLOAD_TIMEOUT)
    fun bankingPartnerFileGenerate() {
        reader.read()
    }
}

@SpringBootApplication
@EnableScheduling
class Application
fun main() {
    println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    println(" _   _ _            _                 _")
    println("| \\ | (_)          | |               (_)")
    println("|  \\| |_ _ __   ___| |_ _   _   _ __  _ _ __   ___")
    println("| . ` | | '_ \\ / _ \\ __| | | | | '_ \\| | '_ \\ / _ \\")
    println("| |\\  | | | | |  __/ |_| |_| | | | | | | | | |  __/")
    println("\\_| \\_/_|_| |_|\\___|\\__|\\__, | |_| |_|_|_| |_|\\___|")
    println("                         _/ |")
    println("                        |___/")
    println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    println("+++++++++MONGO UPLOADER FTP READER v 1.0.0++++++++++")

    SpringApplication.run(Application::class.java)

}
