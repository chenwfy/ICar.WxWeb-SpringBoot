package com.mycarx.wxweb.remoteApi.wxAppPush

import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.QueryMethod
import com.mycarx.wxweb.remoteApi.query
import com.mycarx.wxweb.web.domain.RestResultBase
import com.mycarx.wxweb.web.domain.push.WashOrderNotifyRequest

class PushWashOrderNotifyApi : ApiBase, ApiInterface<RestResultBase?> {
    constructor(request: WashOrderNotifyRequest) : super(QueryMethod.POST) {
        super.url = "${AppConfig.weChatAppPushHost}/washOrderNotify"
        super.dataString = request.toString()
        super.dataMap = request.toMap()
    }

    override fun execute(): RestResultBase? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank()) {
            return resultTxt.jsonDeserialize<RestResultBase>()
        }
        return null
    }
}