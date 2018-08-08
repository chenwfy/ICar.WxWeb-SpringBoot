package com.mycarx.wxweb.web.controller

import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.config.Environment
import com.mycarx.wxweb.web.domain.weChat.JsApiSigResult
import com.mycarx.wxweb.remoteApi.weChat.WechatConfig
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletRequest

/**
 * 需要调用微信JS SDK的VIEW Controller 继承此类。不需要的，则直接继承BaseController
 */
@Controller
open class WeChatAuthViewController : BaseController() {
    /**
     * 注入微信JS SDK页面签名
     */
    @ModelAttribute
    fun commonViewModelAttrs(model: Model, httRequest: HttpServletRequest) {
        model.addAttribute("weChatWebAppName", AppConfig.weChatWebName)

        if (AppConfig.environment == Environment.DEV) {
            return
        }
        val signResult: JsApiSigResult = createCurrentPageWxJsSign(httRequest)
        model.addAttribute("pageJsSign", signResult)

        val shareData = mapOf("title" to AppConfig.shareTitle, "desc" to AppConfig.shareDesc, "icon" to AppConfig.shareIcon, "url" to AppConfig.shareUrl)
        model.addAttribute("shareData", shareData)

        model.addAttribute("shareQrImage", AppConfig.shareQrCodeImg)
    }
}