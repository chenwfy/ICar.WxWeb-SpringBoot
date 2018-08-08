package com.mycarx.wxweb.web.controller

import com.mycarx.utils.DistanceUtils
import com.mycarx.utils.OrderIdUtils
import com.mycarx.utils.divide
import com.mycarx.utils.getRemoteIp
import com.mycarx.weiXinPay.WeiXinPayConfirmResult
import com.mycarx.weiXinPay.WeiXinPayResult
import com.mycarx.wxweb.web.domain.RestResult
import com.mycarx.wxweb.web.domain.RestResultBase
import com.mycarx.wxweb.web.domain.RestResultData
import com.mycarx.wxweb.web.domain.common.OrderNotifyStatus
import com.mycarx.wxweb.web.domain.common.OrderPayStatus
import com.mycarx.wxweb.web.domain.common.OrderPayType
import com.mycarx.wxweb.web.domain.coupon.CouponState
import com.mycarx.wxweb.web.domain.coupon.UserWashCouponEntity
import com.mycarx.wxweb.web.domain.user.CarOwnerEntity
import com.mycarx.wxweb.web.domain.user.UserLevel
import com.mycarx.wxweb.web.domain.wash.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * 洗车部分页面AJAX数据请求路由控制类
 */
@RestController
@RequestMapping("/washService", method = [RequestMethod.GET, RequestMethod.POST])
class WashRestController : BaseController() {
    
}