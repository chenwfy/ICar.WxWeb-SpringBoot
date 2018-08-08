package com.mycarx.wxweb.web.service

import com.mycarx.wxweb.remoteApi.baiduMap.IpConvertToLocationApi
import com.mycarx.wxweb.web.domain.baiduMap.LocalPoint
import org.springframework.stereotype.Service

@Service
class BaiDuMapService {
    /**
     * 利用百度地图接口，将IP转换为经纬度
     */
    fun convertIpAddressToPoint(ipAddress: String): LocalPoint? {
        return IpConvertToLocationApi(ipAddress).execute()
    }
}