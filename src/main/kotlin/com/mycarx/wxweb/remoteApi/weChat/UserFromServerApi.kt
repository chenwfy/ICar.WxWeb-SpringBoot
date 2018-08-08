package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.web.domain.weChat.UserFromServerResult
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.query

class UserFromServerApi : ApiBase, ApiInterface<UserFromServerResult?> {
    private val apiUrl: String = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN"

    constructor(openId: String) : super() {
        super.url = String.format(this.apiUrl, AccessToken.value, openId)
        super.dataString = ""
    }

    override fun execute(): UserFromServerResult? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("openid")) {
            return resultTxt.jsonDeserialize<UserFromServerResult>()
        }
        return null
    }
}