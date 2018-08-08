package com.mycarx.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.text.SimpleDateFormat
import java.util.*

class CustomDateTimeSerializer : JsonSerializer<Date>() {
    override fun serialize(value: java.util.Date, jsonGen: JsonGenerator, provider: SerializerProvider) {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        jsonGen.writeString(formatter.format(value))
    }
}