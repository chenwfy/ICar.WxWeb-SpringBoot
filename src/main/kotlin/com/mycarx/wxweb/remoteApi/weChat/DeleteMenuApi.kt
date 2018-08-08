package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.web.domain.weChat.ErrorResult
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.query

class DeleteMenuApi : ApiBase, ApiInterface<Boolean> {
    private val apiUrl: String = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=${AccessToken.value}"

    constructor() : super() {
        super.url = this.apiUrl
        super.dataString = ""
    }

    override fun execute(): Boolean {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("errcode")) {
            val result: ErrorResult? = resultTxt.jsonDeserialize<ErrorResult>()
            if (null != result) {
                return result.code == 0
            }
        }
        return false
    }
}