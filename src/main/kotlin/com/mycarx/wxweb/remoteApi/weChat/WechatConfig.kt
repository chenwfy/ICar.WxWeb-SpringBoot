package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.wxweb.config.AppConfig
import com.mycarx.utils.StringUtils
import com.mycarx.utils.encryptSha1
import com.mycarx.wxweb.web.domain.weChat.JsApiSigResult
import java.net.URLEncoder
import java.util.*

object WechatConfig {
    private const val authorizeRedirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect"

    fun getAuthorizeRedirectUrl(redirectBackUrl: String): String {
        val nonceStr = StringUtils.getRandomLetters()
        val redirectUrl = "$redirectBackUrl${if (redirectBackUrl.contains("?")) "&" else "?"}random=$nonceStr"
        return String.format(this.authorizeRedirectUrl, AppConfig.weChatAppId, URLEncoder.encode(redirectUrl, "UTF-8"), nonceStr)
    }

    fun createJsApiSignature(pageUrl: String): JsApiSigResult {
        val timestamp = Date().time
        val nonceStr = StringUtils.getRandomLetters()
        val sourceStr = "jsapi_ticket=${JsApiTicket.value}&noncestr=$nonceStr&timestamp=$timestamp&url=$pageUrl"
        val sign = sourceStr.encryptSha1()

        return JsApiSigResult(AppConfig.weChatAppId, timestamp, nonceStr, sign)
    }
}