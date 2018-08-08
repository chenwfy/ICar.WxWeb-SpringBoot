package com.mycarx.wxweb.interceptor

import com.mycarx.utils.*
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.config.Environment
import com.mycarx.wxweb.web.domain.user.AuthUser
import com.mycarx.wxweb.web.domain.user.CarOwnerEntity
import com.mycarx.wxweb.web.domain.user.LoginForm
import com.mycarx.wxweb.remoteApi.weChat.WechatConfig
import com.mycarx.wxweb.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import java.net.URLEncoder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * 授权认证检测
 */
class BusinessUserAuthInterceptor : UserAuthInterceptorInterface {
    @Autowired
    private lateinit var userService: UserService

    /**
     * 授权认证检测
     */
    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse): Boolean {
        val userIp: String = request.getRemoteIp()
        val session: HttpSession = request.session
        var authUser: AuthUser? = SessionUtils.getAuthSession(session)
        if (null != authUser) {
            return true
        }

        //是否为AJAX请求
        if (request.isAjaxRequest()) {
            HttpUtils.renderAuthExpired(response)
            return false
        }

        //开发环境
        if (AppConfig.environment == Environment.DEV) {
            val userLoginForm = LoginForm("ofhjvjsQ-XQQFGGjMw2VGyhRznYc", "ozfeCwi1ZFhAbGs_1bt2l7oEWLqs")
            val loginUser: CarOwnerEntity? = userService.userLogin(userLoginForm)
            if (null != loginUser) {
                authUser = loginUser.toAuthUser()
                SessionUtils.setAuthSession(session, authUser)
                return true
            }
            return false
        }

        //测试或者正式环境（必须是能接入微信的）
        var currentUrl = request.requestURI
        val queryString = request.queryString ?: ""
        if (queryString.isNotEmpty()) {
            currentUrl += "?$queryString"
        }
        currentUrl = URLEncoder.encode(currentUrl, "UTF-8")
        val redirectBackUrl = "${AppConfig.webHost}/wechat/weixin_redirect_back?redirect=$currentUrl&ip=$userIp"

        val redirectToWxUrl = WechatConfig.getAuthorizeRedirectUrl(redirectBackUrl)
        response.sendRedirect(redirectToWxUrl)
        return false
    }
}