package com.ninety.nine.main.restbackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
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
    println("+++++++++TRANSFER BACKEND v 1.0.0++++++++++")

    SpringApplication.run(Application::class.java)

}