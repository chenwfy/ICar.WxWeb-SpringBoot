package com.mycarx.wxweb.web.controller

import com.mycarx.utils.LogUtils
import com.mycarx.utils.SessionUtils
import com.mycarx.wxweb.web.domain.user.AuthUser
import com.mycarx.wxweb.web.domain.user.CarOwnerEntity
import com.mycarx.wxweb.web.domain.user.LoginForm
import com.mycarx.wxweb.web.domain.weChat.AuthAccessTokenResult
import com.mycarx.wxweb.web.domain.weChat.UserFromWebResult
import com.mycarx.wxweb.remoteApi.weChat.AuthAccessTokenApi
import com.mycarx.wxweb.remoteApi.weChat.UserFromWebApi
import com.mycarx.wxweb.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URLDecoder
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import kotlin.concurrent.thread

@Controller
@RequestMapping("/wechat")
class WeChatRedirectController {
    @Autowired
    private lateinit var userService: UserService

    /**
     * 微信服务器回调
     */
    @RequestMapping("/weixin_redirect_back")
    fun weiXinRedirectBack(
            @RequestParam code: String,
            @RequestParam state: String,
            @RequestParam random: String,
            @RequestParam redirect: String,
            @RequestParam(name = "ip", required = false) ip: String?,
            session: HttpSession,
            response: HttpServletResponse): String {
        if (code.isNotEmpty() && state.isNotEmpty() && random.isNotEmpty() && random.equals(state) && redirect.isNotEmpty()) {
            val redirectUrl = URLDecoder.decode(redirect, "UTF-8")
            val authToken: AuthAccessTokenResult? = AuthAccessTokenApi(code).execute()
            if (null != authToken) {
                val wxUserInfo: UserFromWebResult? = UserFromWebApi(authToken.accessToken, authToken.openId).execute()
                if (null != wxUserInfo) {
                    val userLoginForm = LoginForm(
                            wxUserInfo.openId,
                            wxUserInfo.unionId ?: "",
                            wxUserInfo.nickName,
                            wxUserInfo.headImageUrl,
                            wxUserInfo.country,
                            wxUserInfo.province,
                            wxUserInfo.city,
                            wxUserInfo.sex
                    )
                    val loginUser: CarOwnerEntity? = userService.userLogin(userLoginForm)
                    LogUtils.logger.info(loginUser.toString())

                    if (null != loginUser) {
                        val authUser: AuthUser = loginUser.toAuthUser()
                        SessionUtils.setAuthSession(session, authUser)

                        thread(start = true) {
                            userService.updateUserBySubscribeStatus(loginUser.openId)
                        }

                        return "redirect:$redirectUrl"
                    }
                }
            }

        }

        return "redirect:/error"
    }
}