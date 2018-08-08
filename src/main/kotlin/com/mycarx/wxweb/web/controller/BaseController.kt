package com.mycarx.wxweb.web.controller

import com.mycarx.utils.LogUtils
import com.mycarx.utils.SessionUtils
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.remoteApi.weChat.WechatConfig
import com.mycarx.wxweb.web.domain.user.AuthUser
import com.mycarx.wxweb.web.domain.weChat.JsApiSigResult
import com.mycarx.wxweb.web.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

/**
 * 需要登录鉴权的Controller继承此类
 */
@Controller
open class BaseController {
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var oilService: OilService
    @Autowired
    lateinit var washService: WashService
    @Autowired
    lateinit var couponService: CouponService
    @Autowired
    lateinit var utilsService: UtilsService


    @ModelAttribute
    fun setBaiDuMapAppKeyToView(model: Model) {
        model.addAttribute("baiDuMapAppKey", AppConfig.baiDuMapAppKey)
    }

    /**
     * 获取当前页面微信JS SDK签名信息
     */
    protected fun createCurrentPageWxJsSign(httRequest: HttpServletRequest): JsApiSigResult {
        var currentUrl: String = httRequest.requestURL?.toString() ?: ""
        val queryString = httRequest.queryString ?: ""
        if (queryString.isNotEmpty()) {
            currentUrl += "?$queryString"
        }

        return WechatConfig.createJsApiSignature(currentUrl)
    }

    /**
     * 获取当前登录用户信息
     */
    fun currentAuth(): AuthUser {
        val sessionContext: HttpSession? = SessionUtils.getSessionContext()
        return SessionUtils.getAuthSession(sessionContext) ?: AuthUser()
    }

    /**
     * 更新用户信息
     */
    fun updateAuthUserSession(authUser: AuthUser) {
        val sessionContext: HttpSession? = SessionUtils.getSessionContext()
        return SessionUtils.setAuthSession(sessionContext, authUser)
    }

    /**
     * 将当前登录的用户信息注入到VIEW模板中
     */
    fun setAuthUserToModel(model: Model) {
        val user: AuthUser = currentAuth()
        model.addAttribute("userAuth", user)
    }
}