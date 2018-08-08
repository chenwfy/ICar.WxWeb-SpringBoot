package com.mycarx.wxweb.web.controller

import com.mycarx.utils.*
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.web.domain.RestResult
import com.mycarx.wxweb.web.domain.RestResultBase
import com.mycarx.wxweb.web.domain.RestResultData
import com.mycarx.wxweb.web.domain.baiduMap.LocalPoint
import com.mycarx.wxweb.web.domain.weChat.JsApiSigResult
import com.mycarx.wxweb.web.service.BaiDuMapService
import com.mycarx.wxweb.web.service.SmsVerifyCodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/utilsService", method = [RequestMethod.GET, RequestMethod.POST])
class UtilsController : BaseController() {
    @Autowired
    private lateinit var baiDuMapService: BaiDuMapService
    @Autowired
    private lateinit var smsVerifyCodeService: SmsVerifyCodeService
}