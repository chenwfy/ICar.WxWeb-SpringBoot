package com.mycarx.wxweb.remoteApi.baiduMap

import com.mycarx.utils.jsonDeserialize
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.remoteApi.ApiBase
import com.mycarx.wxweb.remoteApi.ApiInterface
import com.mycarx.wxweb.remoteApi.filterHttpResponseResult
import com.mycarx.wxweb.remoteApi.query
import com.mycarx.wxweb.web.domain.baiduMap.LocalPoint

class IpConvertToLocationApi : ApiBase, ApiInterface<LocalPoint?> {
    private val apiUrl: String = "http://api.map.baidu.com/location/ip?ak=%s&ip=%s&coor=bd09ll"

    constructor(ip: String) : super() {
        super.url = String.format(this.apiUrl, AppConfig.baiDuMapAppKey, ip)
        super.dataString = ""
    }

    override fun execute(): LocalPoint? {
        val resultTxt: String = this.query()
        if (resultTxt.isNotBlank() && resultTxt.contains("\"status\":0")) {
            val nodeJsonText = filterHttpResponseResult(resultTxt, "content", "point")
            if (nodeJsonText.isNotEmpty()) {
                return nodeJsonText.jsonDeserialize<LocalPoint>()
            }
        }
        return null
    }
}