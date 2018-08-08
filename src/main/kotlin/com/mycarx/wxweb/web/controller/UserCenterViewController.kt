package com.mycarx.wxweb.web.controller

import com.mycarx.utils.LogUtils
import com.mycarx.utils.divide
import com.mycarx.utils.mobileNumberMasked
import com.mycarx.wxweb.web.domain.user.CarOwnerEntity
import com.mycarx.wxweb.web.domain.user.SimpleOrderItem
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

/**
 * 用户中心相关页面访问路由
 */
@Controller
@RequestMapping("/uc", method = [RequestMethod.GET])
class UserCenterViewController : WeChatAuthViewController() {
    
}