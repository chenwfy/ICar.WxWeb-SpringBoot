package com.mycarx.wxweb.remoteApi

import com.fasterxml.jackson.databind.JsonNode
import com.mycarx.utils.LogUtils
import com.mycarx.utils.parseToJsonNode
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import java.util.ArrayList

fun ApiBase.query(vararg args: String): String {
    val queryInfo: String = "ApiQuery[${this.method}: ${this.url} - ${this.dataString}]"

    try {
        LogUtils.logger.info("$queryInfo Start")

        val httpResponse = createHttpResponse(this.url, this.method, this.dataString, this.dataMap)
        if (httpResponse.statusLine.statusCode == HttpStatus.SC_OK) {
            val responseText: String = EntityUtils.toString(httpResponse.entity, "UTF-8")

            LogUtils.logger.info("$queryInfo Result[${args.joinToString(".")}] ====> $responseText")

            return filterHttpResponseResult(responseText, *args)
        }
    } catch (e: Exception) {
        LogUtils.logger.error("$queryInfo Error ====> $e")
        //e.printStackTrace()
    }
    return ""
}

fun createHttpResponse(url: String, method: QueryMethod, dataString: String, dataMap: Map<Any, Any?>): HttpResponse {
    var httpRequest: HttpUriRequest = HttpGet(url)
    if (method == QueryMethod.POST) {
        var entity: HttpEntity? = null
        if (dataString.isNotBlank() && dataMap.isEmpty()) {
            entity = ByteArrayEntity(dataString.toByteArray())
        } else {
            if (dataMap.isNotEmpty()) {
                val paramList = ArrayList<NameValuePair>(dataMap.size)
                dataMap.forEach { t, u -> paramList.add(BasicNameValuePair(t.toString(), u.toString())) }
                entity = UrlEncodedFormEntity(paramList, "UTF-8")
            }
        }

        if (entity != null) {
            httpRequest = HttpPost(url)
            httpRequest.entity = entity
        }
    }

    val httpClient = HttpClientBuilder.create().build()
    return httpClient.execute(httpRequest)
}

fun filterHttpResponseResult(responseText: String, vararg args: String): String {    
    if (args.isEmpty()) {
        return responseText
    }
    
    if (responseText.isNotBlank() && responseText.isNotEmpty()) {
        var resultJson: String = responseText
        var jsonNode: JsonNode? = resultJson.parseToJsonNode()
        for (it in args) {
            if (null != jsonNode && jsonNode.has(it)) {
                resultJson = jsonNode.get(it).toString()
                if (!resultJson.isNullOrBlank()) {
                    jsonNode = resultJson.parseToJsonNode()
                } else {
                    break
                }
            } else {
                resultJson = ""
            }
        }

        return resultJson
    }

    return ""
}