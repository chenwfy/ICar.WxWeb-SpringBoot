package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.wxweb.config.AppConfig
import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.web.domain.weChat.AccessTokenResult
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.query

class AccessTokenApi : ApiBase, ApiInterface<AccessTokenResult?> {
    private val apiUrl: String = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s"

    constructor() : super() {
        super.url = String.format(this.apiUrl, AppConfig.weChatAppId, AppConfig.weChatAppSecret)
        super.dataString = ""
    }

    override fun execute(): AccessTokenResult? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("access_token")) {
            return resultTxt.jsonDeserialize<AccessTokenResult>()
        }
        return null
    }
}