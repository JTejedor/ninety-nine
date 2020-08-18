package com.ninety.nine.test.file

import com.ninety.nine.main.*
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.ContextConfiguration
import java.io.File

fun checkFile(fileConfiguration: FileConfiguration) = object : Matcher<String> {

    private fun checkFile(value: String): Boolean {
        var lines: Long = 0
        val file = File(value)
        if (!file.exists())
            return false
        file.forEachLine { ++lines }
        return lines < fileConfiguration.transferAmountLines
    }

    override fun test(value: String) =
        MatcherResult(
            checkFile(value),
            "String $value is not a valid transfer line",
            "String $value should not be a valid transfer line"
        )
}

@SpringBootTest
@ContextConfiguration(classes = [(TestConfiguration::class)])
class FileGeneratorTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    private val transferConfiguration =
        TransferConfiguration()

    private val transferGenerator = TransferGenerator(
        transferConfiguration,
        PersonFieldCustomGenerator(transferConfiguration, IbanGenerator(transferConfiguration), NIFGenerator(transferConfiguration)),
        CurrencyNameCustomGenerator(transferConfiguration),
        CurrencyAmountCustomGenerator(transferConfiguration)
    )

    private val fileConfiguration = FileConfiguration()
    init {
        fileConfiguration.transferAmountLines = 100
    }
    private val generator =
        FileGenerator(fileConfiguration, transferGenerator)

    private fun deleteFile(fileName: String){
        File(fileName).delete();
    }

    init {
        test("File should be well created") {
            val fileName = generator.create()
            fileName should checkFile(fileConfiguration)
            deleteFile(fileName)
        }
    }
}
