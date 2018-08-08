package com.mycarx.wxweb.web.controller

import com.mycarx.utils.*
import com.mycarx.wxweb.web.domain.RestResult
import com.mycarx.wxweb.web.domain.RestResultBase
import com.mycarx.wxweb.web.domain.RestResultData
import com.mycarx.wxweb.web.domain.RestResultPaging
import com.mycarx.wxweb.web.domain.common.KVPair
import com.mycarx.wxweb.web.domain.coupon.CouponConfigAction
import com.mycarx.wxweb.web.domain.coupon.UserOilCouponEntity
import com.mycarx.wxweb.web.domain.coupon.UserWashCouponEntity
import com.mycarx.wxweb.web.domain.user.CarOwnerEntity
import com.mycarx.wxweb.web.domain.user.SimpleOrderItem
import com.mycarx.wxweb.web.domain.user.UserCarEntity
import com.mycarx.wxweb.web.service.SmsVerifyCodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * 用户中心相关数据服务访问路由
 */
@RestController
@RequestMapping("/userService", method = [RequestMethod.GET, RequestMethod.POST])
class UserCenterRestController : BaseController() {
    @Autowired
    private lateinit var smsVerifyCodeService: SmsVerifyCodeService

}