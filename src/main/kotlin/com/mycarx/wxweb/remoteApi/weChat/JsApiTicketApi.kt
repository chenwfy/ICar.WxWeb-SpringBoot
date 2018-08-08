package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.web.domain.weChat.JsApiTicketResult
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.query

class JsApiTicketApi : ApiBase, ApiInterface<JsApiTicketResult?> {
    private val apiUrl: String = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=${AccessToken.value}&type=jsapi"

    constructor() : super() {
        super.url = this.apiUrl
        super.dataString = ""
    }

    override fun execute(): JsApiTicketResult? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("ticket")) {
            return resultTxt.jsonDeserialize<JsApiTicketResult>()
        }
        return null
    }
}