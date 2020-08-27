package com.ninety.nine.main

import org.iban4j.Iban
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.ThreadLocalRandom

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="transfer")
class TransferConfiguration {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(TransferConfiguration::class.java)
    }

    var people: Int = 100
    var currencies = arrayOf("USD", "EUR", "GBP", "CHF", "RUB", "DKK")
    var maxAmount = 100000.00
    var probabilityWrong = 0.85
    var probabilityGeneration = 0.9
    var probabilityChangeAmount = 0.5

    fun printConfiguration() {
        logger.info("")
        logger.info("+++++++++++++++++++++++++++++++++++")
        logger.info("++++TRANSFER LINE CONFIGURATION++++")
        logger.info("number of people: $people")
        logger.info("used currencies: ${currencies.contentToString()}")
        logger.info("probability of a field in transfer has bad format: $probabilityWrong")
        logger.info("probability of a bad format: $probabilityGeneration")
        logger.info("probability of amount was in third position or last one: $probabilityChangeAmount")
        logger.info("+++++++++++++++++++++++++++++++++++")
    }
}

interface CustomGenerator {
    fun generateWell(): String
    fun generateWrong(): String
}

interface Generator {
    fun generate(): String
}

class RandomGenerator(private val configuration: TransferConfiguration, private val customGenerator: CustomGenerator) :
    Generator {
    private fun generateWrong(): String {
        val random = Math.random()
        return if (random > configuration.probabilityWrong) {
            return customGenerator.generateWrong()
        } else {
            ""
        }
    }

    override fun generate(): String {
        val random = Math.random()
        return if (random > configuration.probabilityGeneration)
            generateWrong()
        else
            generateWell()
    }

    private fun generateWell(): String {
        return customGenerator.generateWell()
    }
}

@Component
class IbanGenerator(private val configuration: TransferConfiguration) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(IbanGenerator::class.java)

        private const val NUMBER_OF_IBAN_PER_PERSON = 4
    }

    private final val ibanArray = calculateIbanArray(configuration.people * NUMBER_OF_IBAN_PER_PERSON)

    private fun calculateIbanArray(size: Int): Array<String> {
        logger.info("GENERATING AN IBAN ARRAY OF ${configuration.people} * $NUMBER_OF_IBAN_PER_PERSON")
        return Array(size) { Iban.random().toString() }
    }

    fun generateWell(randomPosition: Int): String {
        val randomIbanPosition =
            ThreadLocalRandom.current().nextInt(0, NUMBER_OF_IBAN_PER_PERSON)
        val ibanPosition = randomPosition * NUMBER_OF_IBAN_PER_PERSON + randomIbanPosition;
        return ibanArray[ibanPosition]
    }

    fun generateWrong(): String {
        val iban = Iban.random().toString()
        val randomPosition = ThreadLocalRandom.current().nextInt(0, iban.length)
        return iban.replace(iban[randomPosition].toString(), "")
    }
}

@Component
class NIFGenerator(private val configuration: TransferConfiguration) {
    companion object {
        private const val NIF_CHARACTERS = "TRWAGMYFPDXBNJZSQVHLCKE"
        private const val MAXIMUM_NUMBER_NIF = 99999999

        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(NIFGenerator::class.java)
    }

    private final val nifArray = calculateNifArray(configuration.people)

    private fun calculateNifArray(size: Int): Array<String> {
        logger.info("GENERATING A NIF ARRAY OF ${configuration.people}")
        return Array(size) {
            val randomNIF =
                ThreadLocalRandom.current().nextInt(
                    1000,
                    MAXIMUM_NUMBER_NIF
                )
            val char = NIF_CHARACTERS[randomNIF % NIF_CHARACTERS.length]
            String.format("%08d", randomNIF) + char
        }
    }

    fun generateWell(randomPosition: Int): String {
        return nifArray[randomPosition]
    }

    fun generateWrong(): String {
        val randomNIF =
            ThreadLocalRandom.current().nextInt(
                1000,
                MAXIMUM_NUMBER_NIF
            )
        val char = NIF_CHARACTERS[(randomNIF + 1) % NIF_CHARACTERS.length]
        return String.format("%08d", randomNIF) + char
    }
}

@Component
class PersonFieldCustomGenerator(
    private val configuration: TransferConfiguration,
    private val ibanGenerator: IbanGenerator,
    private val nifGenerator: NIFGenerator
) : CustomGenerator {
    override fun generateWell(): String {
        val randomPersonFieldPosition =
            ThreadLocalRandom.current().nextInt(0, configuration.people)
        return ibanGenerator.generateWell(randomPersonFieldPosition) + "\t" +
                nifGenerator.generateWell(randomPersonFieldPosition)
    }

    override fun generateWrong(): String {
        return ibanGenerator.generateWrong() + "\t" +
                nifGenerator.generateWrong()
    }

}

@Component
class CurrencyNameCustomGenerator(private val configuration: TransferConfiguration) :
    CustomGenerator {
    companion object {
        private val BAD_CURRENCY_NAMES = arrayOf("SDU", "RUE", "GB", "CFH", "BUR", "KK", "KES")
    }
    fun checkCurrencies(){
        for(currency in configuration.currencies){
            java.util.Currency.getInstance(currency)
        }
    }

    override fun generateWell(): String {
        val randomCurrencyNamePosition =
            ThreadLocalRandom.current().nextInt(0, configuration.currencies.size)
        return configuration.currencies[randomCurrencyNamePosition]
    }

    override fun generateWrong(): String {
        val randomCurrencyNamePosition =
            ThreadLocalRandom.current().nextInt(0, BAD_CURRENCY_NAMES.size)
        return BAD_CURRENCY_NAMES[randomCurrencyNamePosition]
    }
}

@Component
class CurrencyAmountCustomGenerator(configuration: TransferConfiguration) :
    CustomGenerator {
    private final val amount = configuration.maxAmount
    override fun generateWell(): String {
        val rawNumber = ThreadLocalRandom.current().nextDouble(
            10.0,
            amount
        )
        var bd: BigDecimal = BigDecimal.valueOf(rawNumber)
        bd = bd.setScale(2, RoundingMode.HALF_UP)
        return bd.toDouble().toString()
    }

    override fun generateWrong(): String {
        return "N/A"
    }
}

@Component
class TransferGenerator(
    private val configuration: TransferConfiguration,
    personFieldCustomGenerator: PersonFieldCustomGenerator,
    currencyNameGenerator: CurrencyNameCustomGenerator,
    currencyAmountGenerator: CurrencyAmountCustomGenerator
) : Generator {

    companion object {
        const val MALFORMED_LINE = "Ninety nine rocks"
    }

    private val generatorsAmountLastPosition: Array<Generator> =
        arrayOf(
            RandomGenerator(configuration, personFieldCustomGenerator),
            RandomGenerator(configuration, currencyNameGenerator),
            RandomGenerator(configuration, currencyAmountGenerator)
        )

    private val generatorsAmountThirdPosition: Array<Generator> =
        arrayOf(
            RandomGenerator(configuration, personFieldCustomGenerator),
            RandomGenerator(configuration, currencyAmountGenerator),
            RandomGenerator(configuration, currencyNameGenerator)
        )

    private fun generalGenerate(generators: Array<Generator>): String {
        val builder = StringBuilder()
        val postFix = "\t"
        for (i in 0 until generators.size - 1) {
            builder.append(generators[i].generate())
            builder.append(postFix)
        }
        builder.append(generators.last().generate())
        return builder.toString()
    }

    private fun generateAmountThirdPosition(): String {
        return generalGenerate(generatorsAmountThirdPosition)
    }

    private fun generateAmountLastPosition(): String {
        return generalGenerate(generatorsAmountLastPosition)
    }

    private fun innerGenerate(): String {
        val random = Math.random()
        return if (random > configuration.probabilityChangeAmount)
            generateAmountThirdPosition()
        else
            generateAmountLastPosition()
    }

    private fun innerGenerateWrong():String{
        val random = Math.random()
        return if (random > configuration.probabilityChangeAmount)
            ""
        else
            MALFORMED_LINE
    }

    override fun generate(): String {
        val random = Math.random()
        return if (random > configuration.probabilityWrong)
            innerGenerateWrong()
        else
            innerGenerate()
    }
}
