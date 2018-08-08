package com.mycarx.wxweb.web.controller

import com.mycarx.wxweb.web.domain.coupon.UserWashCouponEntity
import com.mycarx.wxweb.web.domain.user.CarOwnerEntity
import com.mycarx.wxweb.web.domain.user.UserLevel
import com.mycarx.wxweb.web.domain.wash.WashMachineEntity
import com.mycarx.wxweb.web.domain.wash.WashStation
import com.mycarx.wxweb.web.domain.wash.WashStationStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * 洗车部分页面路由控制类
 */
@Controller
@RequestMapping("/wash", method = [RequestMethod.GET])
class WashViewController : WeChatAuthViewController() {
    /**
     * 洗车门店列表
     */
    @GetMapping("/stationList")
    fun stationList(model: Model): String {
        
        return "wash/stationList"
    }
}