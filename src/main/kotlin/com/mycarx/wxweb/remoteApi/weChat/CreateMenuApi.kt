package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.web.domain.weChat.ErrorResult
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.QueryMethod
import com.mycarx.wxweb.remoteApi.query

class CreateMenuApi : ApiBase, ApiInterface<Boolean> {
    private val apiUrl: String = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=${AccessToken.value}"

    constructor(menuJson: String) : super(QueryMethod.POST) {
        super.url = this.apiUrl
        super.dataString = menuJson
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