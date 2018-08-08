package com.mycarx.wxweb.remoteApi.baiduTTS

import com.mycarx.utils.LogUtils
import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.query
import com.mycarx.wxweb.web.domain.baiduTTS.TTSTokenResult

class TTSTokenApi : ApiBase, ApiInterface<TTSTokenResult?> {
    private val apiUrl: String = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s"

    constructor() : super() {
        super.url = String.format(this.apiUrl, AppConfig.baiDuTTSAppKey, AppConfig.baiDuTTSAppSecret)
        super.dataString = ""
    }

    override fun execute(): TTSTokenResult? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("access_token")) {
            return resultTxt.jsonDeserialize<TTSTokenResult>()
        }
        return null
    }
}