package com.ninety.nine.test.transfer

import com.ninety.nine.main.*
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.*
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.string.shouldMatch
import io.kotest.spring.SpringListener
import org.iban4j.IbanUtil
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.ThreadLocalRandom

fun hasProperLetter() = object : Matcher<String> {

    private val NIF_CHARACTERS = "TRWAGMYFPDXBNJZSQVHLCKE"

    fun isAValidNIF(nif: String): Boolean {
        val number = nif.substring(0, nif.length - 1).toLongOrNull()
        return when {
            number == null -> false
            nif.length != 9 -> false
            else -> nif[nif.length - 1] == NIF_CHARACTERS[(number % 23).toInt()]
        }
    }

    override fun test(value: String) =
        MatcherResult(
            isAValidNIF(value),
            "String $value should has proper letter",
            "String $value should not has a proper letter"
        )
}

@SpringBootTest
@ContextConfiguration(classes = [(TestConfiguration::class)])
class NIFCustomerGeneratorTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val configuration = TransferConfiguration()
    private val generator =
        NIFGenerator(configuration)

    init {
        test("Well formatted NIF should have 8 digits and one character").config(invocations = 10) {
            val randomPosition = ThreadLocalRandom.current().nextInt(0, configuration.people)
            generator.generateWell(randomPosition) shouldMatch "([a-z]|[A-Z]|[0-9])[0-9]{7}([a-z]|[A-Z]|[0-9])"
        }
        test("Well formatted NIF should have the proper last character").config(invocations = 10) {
            val randomPosition = ThreadLocalRandom.current().nextInt(0, configuration.people)
            generator.generateWell(randomPosition) should hasProperLetter()
        }
        test("Wrong formatted NIF should have 8 digits and one character").config(invocations = 10) {
            generator.generateWrong() shouldMatch "([a-z]|[A-Z]|[0-9])[0-9]{7}([a-z]|[A-Z]|[0-9])"
        }
        test("Wrong formatted NIF should not have the proper last character").config(invocations = 10) {
            generator.generateWrong() shouldNot hasProperLetter()
        }
    }
}

fun isAProperIban() = object : Matcher<String> {

    fun isValid(value: String): Boolean {
        return try {
            IbanUtil.validate(value)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override fun test(value: String) =
        MatcherResult(
            isValid(value),
            "String $value should be proper iban",
            "String $value should not be proper iban"
        )
}

@SpringBootTest
@ContextConfiguration(classes = [(TestConfiguration::class)])
class IbanCustomerGeneratorTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val configuration = TransferConfiguration()
    private val generator =
        IbanGenerator(configuration)

    init {
        test("Well formatted IBAN").config(invocations = 50) {
            val randomPosition = ThreadLocalRandom.current().nextInt(0, configuration.people)
            generator.generateWell(randomPosition) should isAProperIban()
        }
        test("Wrong formatted IBAN").config(invocations = 50) {
            generator.generateWrong() shouldNot isAProperIban()
        }
    }
}

fun isAValidCurrencyName(configuration: TransferConfiguration) = object : Matcher<String> {

    private val validCurrencies = configuration.currencies

    override fun test(value: String) =
        MatcherResult(
            validCurrencies.contains(value),
            "String $value is not a valid currency",
            "String $value should not be a valid currency"
        )
}

fun isAInvalidCurrencyName() = object : Matcher<String> {
    private val badCurrencyNames = arrayOf("SDU", "RUE", "GB", "CFH", "BUR", "KK", "KES")

    override fun test(value: String) =
        MatcherResult(
            badCurrencyNames.contains(value),
            "String $value is a valid currency",
            "String $value should be a valid currency"
        )
}

@SpringBootTest
@ContextConfiguration(classes = [(TestConfiguration::class)])
class CurrencyNameCustomerGeneratorTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val configuration = TransferConfiguration()
    private val generator =
        CurrencyNameCustomGenerator(configuration)

    init {
        test("Valid currency").config(invocations = 50) {
            generator.generateWell() should isAValidCurrencyName(configuration)
        }
        test("Wrong valid currency").config(invocations = 50) {
            generator.generateWrong() should isAInvalidCurrencyName()
        }
    }
}

@SpringBootTest
@ContextConfiguration(classes = [(TestConfiguration::class)])
class CurrencyAmountCustomerGeneratorTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val configuration = TransferConfiguration()
    private val generator =
        CurrencyAmountCustomGenerator(configuration)

    init {
        test("Valid currency").config(invocations = 50) {
            generator.generateWell().toDouble() shouldBeLessThan configuration.maxAmount
        }
        test("Wrong valid currency").config(invocations = 50) {
            generator.generateWrong() shouldBe "N/A"
        }
    }
}

@SpringBootTest
@ContextConfiguration(classes = [(TestConfiguration::class)])
class PersonFieldCustomerGeneratorTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val configuration = TransferConfiguration()
    private val generator =
        PersonFieldCustomGenerator(configuration, IbanGenerator(configuration), NIFGenerator(configuration))

    init {
        test("Valid Person Field").config(invocations = 50) {
            val badGeneratedPersonField = generator.generateWell()
            val splitPersonField = badGeneratedPersonField.split("\t");
            splitPersonField.size shouldBe 2
            splitPersonField[0] should isAProperIban()
            splitPersonField[1] should hasProperLetter()
        }
        test("Wrong Person Field").config(invocations = 50) {
            val badGeneratedPersonField = generator.generateWrong()
            val splitPersonField = badGeneratedPersonField.split("\t");
            splitPersonField.size shouldBe 2
            splitPersonField[0] shouldNot isAProperIban()
            splitPersonField[1] shouldNot hasProperLetter()
        }
    }
}

fun isAValidTransfer() = object : Matcher<String> {

    private fun isValidTransfer(value: String): Boolean {
        return value == TransferGenerator.MALFORMED_LINE ||
                value.isBlank() ||
                value.split("\t").size >= 2
    }

    override fun test(value: String) =
        MatcherResult(
            isValidTransfer(value),
            "String \"$value\" is not a valid transfer line",
            "String \"$value\" should not be a valid transfer line"
        )
}

@SpringBootTest
@ContextConfiguration(classes = [(TestConfiguration::class)])
class TransferGeneratorTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val configuration = TransferConfiguration()
    private val generator = TransferGenerator(
        configuration,
        PersonFieldCustomGenerator(
            configuration,
            IbanGenerator(configuration),
            NIFGenerator(configuration)
        ),
        CurrencyNameCustomGenerator(configuration),
        CurrencyAmountCustomGenerator(configuration)
    )

    init {
        test("Valid Transfer Line").config(invocations = 500) {
            generator.generate() should isAValidTransfer()
        }
    }
}
