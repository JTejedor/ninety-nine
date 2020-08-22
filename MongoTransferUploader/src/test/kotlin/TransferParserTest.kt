package com.ninety.nine.mongouploader.test

import com.ninety.nine.main.mongouploader.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.FunSpec
import io.kotest.spring.SpringAutowireConstructorExtension
import io.kotest.spring.SpringListener
import io.mockk.mockkClass
import org.iban4j.Iban
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.util.concurrent.ThreadLocalRandom

class ProjectConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(SpringAutowireConstructorExtension)
}

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [ConfigFileApplicationContextInitializer::class],
    classes = [TestConfiguration::class, IbanParser::class]
)
class IbanParserGeneratorTest(private val parser: IbanParser) : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    init {
        test("Test throw exception because is a bad Iban").config(invocations = 10) {
            val iban = Iban.random().toString()
            val randomPosition = ThreadLocalRandom.current().nextInt(0, iban.length)
            val badIban = iban.replace(iban[randomPosition].toString(), "")
            shouldThrow<ParsingTransferException> {
                parser.parse(badIban)
            }
        }
        test("Test well processed IBANs").config(invocations = 100) {
            val goodIban = Iban.random().toString()
            parser.parse(goodIban)
        }
    }
}

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [ConfigFileApplicationContextInitializer::class],
    classes = [TestConfiguration::class, NIFParser::class]
)
class NifParserGeneratorTest(private val parser: NIFParser) : FunSpec() {

    companion object {
        private const val NIF_CHARACTERS = "TRWAGMYFPDXBNJZSQVHLCKE"
        private const val MAXIMUM_NUMBER_NIF = 99999999
    }

    private fun calculateNif(): String {
        val randomNIF =
            ThreadLocalRandom.current().nextInt(
                1000,
                MAXIMUM_NUMBER_NIF
            )
        val char = NIF_CHARACTERS[randomNIF % NIF_CHARACTERS.length]
        return String.format("%08d", randomNIF) + char
    }

    private fun calculateBadNif(): String {
        val randomNIF =
            ThreadLocalRandom.current().nextInt(
                1000,
                MAXIMUM_NUMBER_NIF
            )
        val char = NIF_CHARACTERS[(randomNIF + 1) % NIF_CHARACTERS.length]
        return String.format("%08d", randomNIF) + char
    }

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    init {
        test("Test throw exception because it is a bad nif").config(invocations = 10) {
            val nif = calculateBadNif()
            shouldThrow<ParsingTransferException> {
                parser.parse(nif)
            }
        }
        test("Test well processed NIFs").config(invocations = 100) {
            val nif = calculateNif()
            parser.parse(nif)
        }
    }
}

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [ConfigFileApplicationContextInitializer::class],
    classes = [TestConfiguration::class, CurrencyAmountParser::class]
)
class CurrencyAmountParserGeneratorTest(private val parser: CurrencyAmountParser) : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val currencyNames = arrayOf("USD", "EUR", "GBP", "CHF", "RUB", "DKK", "SEK")
    private val badCurrencyNames = arrayOf("SDU", "RUE", "GB", "CFH", "BUR", "KK", "EKS")

    private fun getRandom(array: Array<String>): String {
        val randomCurrencyPosition = ThreadLocalRandom.current().nextInt(0, array.size)
        return array[randomCurrencyPosition]
    }

    private fun getRandomCurrency(): String {
        return getRandom(currencyNames)
    }

    private fun getRandomBadCurrency(): String {
        return getRandom(badCurrencyNames)
    }

    private fun getRandomAmount(): Double {
        return ThreadLocalRandom.current().nextDouble(0.00, 10000.00)
    }

    init {
        test("Test throw exception because is a bad currency").config(invocations = 10) {
            val badCurrency: String = getRandomBadCurrency()
            val amount: String = getRandomAmount().toString()
            shouldThrow<ParsingTransferException> {
                parser.parse(badCurrency, amount)
            }
        }

        test("Test throw exception because is a bad amount").config(invocations = 10) {
            val currency = getRandomCurrency()
            shouldThrow<ParsingTransferException> {
                parser.parse(currency, currency)
            }
        }
        test("Test well processed currency and amount").config(invocations = 1000) {
            val amount = getRandomAmount().toString()
            val currency = getRandomCurrency()
            parser.parse(amount, currency)
            parser.parse(currency, amount)
        }
    }
}



@SpringBootTest()
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [ConfigFileApplicationContextInitializer::class],
    classes = [TransferParser::class, CurrencyAmountParser::class, IbanParser::class, NIFParser::class, CustomDateTimeFormatter::class]
)
class TransferParserGeneratorTest(private val parser: TransferParser) : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    init {
        test("Test throw exception because is a bad date") {
            val testLine = "\t95117.36\tCHF"
            shouldThrow<ParsingTransferException> {
                parser.parse("", testLine)
            }
        }

        test("Test throw exception because is a bad transfer") {
            val testLine = "\t95117.36\tCHF"
            shouldThrow<ParsingTransferException> {
                parser.parse("20200821T123045Z", testLine)
            }
        }
        test("Test well processed transfer").config(invocations = 100) {
            val testLine = "GT35US0YDWQT3S8DWCP7E4XFNF0H\t36311270C\tDKK\t35689.7"
            parser.parse("20200821T123045Z", testLine)
        }
    }
}
