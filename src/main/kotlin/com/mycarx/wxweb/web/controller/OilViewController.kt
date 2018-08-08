package com.mycarx.wxweb.web.controller

import com.mycarx.wxweb.web.domain.coupon.UserOilCouponEntity
import com.mycarx.wxweb.web.domain.oil.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * 加油部分页面路由控制类
 */
@Controller
@RequestMapping("/oil", method = [RequestMethod.GET])
class OilViewController : WeChatAuthViewController() {
    
}