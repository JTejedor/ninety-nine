package com.ninety.nine.main.mongouploader

import org.iban4j.Iban
import org.javamoney.moneta.FastMoney
import org.springframework.stereotype.Component
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.util.*
import javax.money.convert.MonetaryConversions


interface Parser<T> {
    @Throws(ParsingTransferException::class)
    fun parse(vararg data: String): T
}

class ParsingTransferException(error: String) : Exception(error)

@Component
final class TransferParser(
    private val ibanParser: Parser<Iban>,
    private val nifParser: Parser<String>,
    private val currencyAmountParser: CurrencyAmountParser,
    private val customDateTimeFormatter: CustomDateTimeFormatter
) : Parser<Transfer> {
    companion object{
        private val conversionEUR = MonetaryConversions.getConversion("EUR")
        private val df = DecimalFormat("0.00")
    }
    private var timestamp: LocalDateTime? = null

    override fun parse(vararg data: String): Transfer {
        if (data.size != 2) {
            throw ParsingTransferException("Transfer checker error - different from 1")
        }
        if (data[0].isBlank() || data[1].isBlank()) {
            throw ParsingTransferException("Transfer checker error - data is blank")
        }
        timestamp = customDateTimeFormatter.format(data[0])
        val dataSplit = data[1].split("\t")
        if (dataSplit.size != 4) {
            throw ParsingTransferException("Transfer checker error - no proper split, size != 4. size = ${dataSplit.size}")
        }
        val pair = currencyAmountParser.parse(dataSplit[2], dataSplit[3])
        return Transfer(
            null,
            timestamp!!,
            ibanParser.parse(dataSplit[0]).toString(),
            nifParser.parse(dataSplit[1]),
            pair.first.toString(),
            pair.second,
            getEurConversion(pair.second, pair.first.currencyCode)
        )
    }

    private fun getEurConversion(amount: Double, currency: String): Double{
        val currentMoney: FastMoney = FastMoney.of(amount, currency)
        val eurConversion: FastMoney = currentMoney.with(conversionEUR)
        return df.format(eurConversion.number.doubleValueExact()).toDouble()
    }
}

@Component
final class IbanParser : Parser<Iban> {
    override fun parse(vararg data: String): Iban {
        return try {
            Iban.valueOf(data[0])
        } catch (ex: Exception) {
            throw ParsingTransferException("Iban Checker error - iban not recognized")
        }
    }
}

@Component
final class NIFParser : Parser<String> {
    companion object {
        private const val NIF_CHARACTERS = "TRWAGMYFPDXBNJZSQVHLCKE"
    }

    override fun parse(vararg data: String): String {
        if (data.size != 1) {
            throw ParsingTransferException("NIF Checker error -  data size more than 1")
        }
        if (data[0].length != 9) {
            throw ParsingTransferException("NIF Checker error - data length != 9")
        }
        try {
            val number = data[0].substring(0, 8).toInt()
            if (NIF_CHARACTERS[number % NIF_CHARACTERS.length] != data[0].last()) {
                throw ParsingTransferException("NIF Checker error - ")
            }
        } catch (ex: Exception) {
            when (ex) {
                is ParsingTransferException -> {
                    throw ex.message?.let { ParsingTransferException(it) }!!
                }
                is NumberFormatException -> {
                    throw ParsingTransferException("NIF Checker error - not a number")
                }
            }
        }
        return data[0]
    }
}

@Component
final class CurrencyAmountParser : Parser<Pair<Currency, Double>> {
    private fun getCurrency(currency: String): Currency {
        return Currency.getInstance(currency)
    }

    private fun getAmount(amount: String): Double {
        return amount.toDouble()
    }

    private fun getPair(currency: String, amount: String): Pair<Currency, Double> {
        return Pair(getCurrency(currency), getAmount(amount))
    }

    private fun tryParse(currency: String, amount: String): Pair<Currency, Double> {
        return try {
            getPair(currency, amount)
        } catch (ex: Exception) {
            throw ParsingTransferException("Currency Amount Checker error -  data is not currency nor amount")
        }
    }

    override fun parse(vararg data: String): Pair<Currency, Double> {
        if (data.size != 2) {
            throw ParsingTransferException("Currency Amount Checker error -  data size more than 2")
        }
        return  try {
            tryParse(data[0], data[1])
        } catch (ex: Exception) {
            tryParse(data[1], data[0])
        }

    }

}