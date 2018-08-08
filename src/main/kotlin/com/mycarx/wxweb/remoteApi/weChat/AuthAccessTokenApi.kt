package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.wxweb.config.AppConfig
import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.web.domain.weChat.AuthAccessTokenResult
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.query

class AuthAccessTokenApi : ApiBase, ApiInterface<AuthAccessTokenResult?> {
    private val apiUrl: String = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code"

    constructor(code: String) : super() {
        super.url = String.format(this.apiUrl, AppConfig.weChatAppId, AppConfig.weChatAppSecret, code)
        super.dataString = ""
    }

    override fun execute(): AuthAccessTokenResult? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("access_token")) {
            return resultTxt.jsonDeserialize<AuthAccessTokenResult>()
        }
        return null
    }
}