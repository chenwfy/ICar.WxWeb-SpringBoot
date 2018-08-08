package com.mycarx.wxweb.remoteApi.baiduTTS

import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import java.net.URLEncoder

/**
 * 百度语音合成接口
 */
class BaiDuText2AudioApi {
    companion object {
        /**
         * 从文件内容加载语音文件数据
         * text - 文字内容，建议100字以内
         */
        @JvmStatic
        fun loadAudioData(text: String): ByteArray {
            val sourceText: String = if (text.trim().length > 256) text.trim().substring(0, 256) else text.trim()
            if (sourceText.isEmpty()) {
                return byteArrayOf()
            }
            val content: String = URLEncoder.encode(sourceText, "UTF-8")
            val token: String = TTSToken.value
            val apiUrl = "https://tsn.baidu.com/text2audio?tex=$content&lan=zh&cuid=WX&ctp=1&tok=$token&vol=8"
            try {
                val httpRequest: HttpUriRequest = HttpGet(apiUrl)
                val httpClient = HttpClientBuilder.create().build()
                val httpResponse = httpClient.execute(httpRequest)
                if (httpResponse.statusLine.statusCode == HttpStatus.SC_OK) {
                    val contentType: String = httpResponse.getHeaders("Content-Type")?.get(0)?.value?.toString() ?: ""
                    if (contentType.equals("audio/mp3", true)) {
                        return EntityUtils.toByteArray(httpResponse.entity)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return byteArrayOf()
        }
    }
}