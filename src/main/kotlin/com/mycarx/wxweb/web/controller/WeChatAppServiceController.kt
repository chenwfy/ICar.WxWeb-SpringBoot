package com.mycarx.wxweb.web.controller

import com.mycarx.wxweb.web.domain.RestResult
import com.mycarx.wxweb.web.domain.RestResultBase
import com.mycarx.wxweb.web.domain.common.OrderNotifyStatus
import com.mycarx.wxweb.web.domain.common.OrderPayStatus
import com.mycarx.wxweb.web.domain.common.OrderPayType
import com.mycarx.wxweb.web.domain.wash.WashPaymentOrderEntity
import com.mycarx.wxweb.web.service.MessageType
import com.mycarx.wxweb.web.service.PushMessage
import com.mycarx.wxweb.web.service.WashService
import com.mycarx.wxweb.web.service.WebSocketServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * 用于微信小程序服务器端回调的相关接口
 */
@RestController
@RequestMapping("/wxAppService")
class WeChatAppServiceController {
    @Autowired
    private lateinit var washService: WashService

}