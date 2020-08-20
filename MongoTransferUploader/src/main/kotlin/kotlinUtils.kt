package com.ninety.nine.main.mongouploader

import java.io.PrintWriter
import java.io.StringWriter

fun convertStactTrace(ex: Exception): String {
    val sw = StringWriter()
    ex.printStackTrace(PrintWriter(sw))
    return sw.toString()
}