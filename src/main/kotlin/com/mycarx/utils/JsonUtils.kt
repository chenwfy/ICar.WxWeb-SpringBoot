package com.mycarx.utils

import java.io.IOException
import java.io.StringWriter
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*

/**
 * JSON序列化相关辅助类
 */
class JsonUtils {
    companion object {
        @JvmStatic
        fun serialize(source: Any): String {
            try {
                val write = StringWriter()
                ObjectMapper().writeValue(write, source)
                return write.toString()
            } catch (e: JsonGenerationException) {
                e.printStackTrace()
            } catch (e: JsonMappingException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }

        @JvmStatic
        fun parseToJsonNode(json: String): JsonNode? {
            try {
                return ObjectMapper().readTree(json)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            } catch (e: JsonMappingException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        @JvmStatic
        fun <T> deserialize(json: String, clazz: Class<T>): T? {
            try {
                return ObjectMapper().registerKotlinModule().readValue(json, clazz)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            } catch (e: JsonMappingException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        @JvmStatic
        fun <T> deserialize(json: String, typeRef: TypeReference<T>): T? {
            try {
                return ObjectMapper().registerKotlinModule().readValue(json, typeRef)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            } catch (e: JsonMappingException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }
}