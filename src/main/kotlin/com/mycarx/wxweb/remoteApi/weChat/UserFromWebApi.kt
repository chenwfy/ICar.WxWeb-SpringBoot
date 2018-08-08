package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.web.domain.weChat.UserFromWebResult
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.query

class UserFromWebApi : ApiBase, ApiInterface<UserFromWebResult?> {
    private val apiUrl: String = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN"

    constructor(token: String, openId: String) : super() {
        super.url = String.format(this.apiUrl, token, openId)
        super.dataString = ""
    }

    override fun execute(): UserFromWebResult? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("openid")) {
            return resultTxt.jsonDeserialize<UserFromWebResult>()
        }
        return null
    }
}