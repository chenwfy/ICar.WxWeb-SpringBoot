package com.mycarx.wxweb.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * 其他相关页面访问路由
 */
@Controller
@RequestMapping("/other", method = [RequestMethod.GET])
class OtherViewController : WeChatAuthViewController() {
    
}