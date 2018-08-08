package com.mycarx.wxweb.web.controller

import com.mycarx.utils.*
import com.mycarx.weiXinPay.WeiXinPayConfirmResult
import com.mycarx.weiXinPay.WeiXinPayResult
import com.mycarx.wxweb.web.domain.RestResult
import com.mycarx.wxweb.web.domain.RestResultBase
import com.mycarx.wxweb.web.domain.RestResultData
import com.mycarx.wxweb.web.domain.common.OrderNotifyStatus
import com.mycarx.wxweb.web.domain.common.OrderPayStatus
import com.mycarx.wxweb.web.domain.coupon.CouponState
import com.mycarx.wxweb.web.domain.coupon.UserOilCouponEntity
import com.mycarx.wxweb.web.domain.oil.*
import com.mycarx.wxweb.web.domain.wash.WashStation
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * 加油部分页面AJAX数据请求路由控制类
 */
@RestController
@RequestMapping("/oilService", method = [RequestMethod.GET, RequestMethod.POST])
class OilRestController : BaseController() {
    
}