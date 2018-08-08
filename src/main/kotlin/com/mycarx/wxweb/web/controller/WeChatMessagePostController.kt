package com.mycarx.wxweb.web.controller

import com.mycarx.utils.encryptSha1
import com.mycarx.utils.end
import com.mycarx.utils.readXmlAsMap
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.remoteApi.weChat.UserFromServerApi
import com.mycarx.wxweb.web.domain.coupon.CouponConfigAction
import com.mycarx.wxweb.web.domain.weChat.UserFromServerResult
import com.mycarx.wxweb.web.service.CouponService
import com.mycarx.wxweb.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/wxMessage", method = [RequestMethod.GET, RequestMethod.POST])
class WeChatMessagePostController {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var couponService: CouponService

    /**
     * 验证消息转发签名
     */
    private fun checkWeChatSignature(signature: String, timestamp: String, nonce: String): Boolean {
        if (signature.isEmpty() || timestamp.isEmpty() || nonce.isEmpty()) {
            return false
        }

        val sourceList = mutableListOf(AppConfig.weChatMessageToken, timestamp, nonce)
        sourceList.sort()
        val encryptResult = sourceList.joinToString("").encryptSha1()
        return signature.equals(encryptResult, true)
    }

    /**
     * 微信消息转发
     */
    @RequestMapping("/forward", method = [RequestMethod.GET, RequestMethod.POST])
    fun weChatMessageForward(
            @RequestParam(name = "signature") signature: String?,
            @RequestParam(name = "timestamp") timestamp: String?,
            @RequestParam(name = "nonce") nonce: String?,
            @RequestParam(name = "echostr") echostr: String?,
            @RequestParam(name = "openid") openid: String?,
            request: HttpServletRequest,
            response: HttpServletResponse
    ) {
        val wxSign: String = signature ?: ""
        val wxTimeStamp: String = timestamp ?: ""
        val wxNonce: String = nonce ?: ""
        if (!this.checkWeChatSignature(wxSign, wxTimeStamp, wxNonce)) {
            response.end("")
            return
        }

        if (request.method.equals("GET", true)) {
            val wxEchoStr: String = echostr ?: ""
            response.end(wxEchoStr)
            return
        }

        val requestMap = request.readXmlAsMap()
        if (requestMap.isNotEmpty() && !requestMap.containsKey("return_code")) {
            val messageType: String = if (requestMap.containsKey("MsgType")) requestMap["MsgType"] ?: "" else ""

            //事件推送
            if (messageType.equals("event", true)) {
                val eventName: String = if (requestMap.containsKey("Event")) requestMap["Event"] ?: "" else ""
                //关注和取消关注事件
                if (eventName.equals("subscribe", true) || eventName.equals("unsubscribe", true)) {
                    val userOpenId: String = if (requestMap.containsKey("FromUserName")) requestMap["FromUserName"] ?: "" else ""
                    if (userOpenId.isNotEmpty()) {
                        if (eventName.equals("subscribe", true)) {
                            this.userSubscribe(userOpenId)
                        } else {
                            this.userUnSubscribe(userOpenId)
                        }
                    }
                }
            } else {
                //消息推送 - 直接回复转接至客服处理
                val msgMessageTypeList = listOf("text", "image", "voice", "video", "shortvideo", "location", "link")
                if (msgMessageTypeList.contains(messageType)) {
                    val resXml = StringBuilder("<xml>")
                            .append("<ToUserName><![CDATA[${requestMap["ToUserName"]}]]></ToUserName>")
                            .append("<FromUserName><![CDATA[${requestMap["FromUserName"]}]]></FromUserName>")
                            .append("<CreateTime>${requestMap["CreateTime"]}</CreateTime>")
                            .append("<MsgType><![CDATA[transfer_customer_service]]></MsgType>")
                            .append("</xml>")
                            .toString()

                    response.setHeader("content-type", "text/xml")
                    response.end(resXml)
                    return
                }
            }
        }

        //回应给微信服务器，以免重复接收
        response.setHeader("content-type", "text/xml")
        response.end("")
    }

    /**
     * 用户关注公众号
     */
    private fun userSubscribe(openId: String) {
        val wxUserInfo: UserFromServerResult? = UserFromServerApi(openId).execute()
        if (null != wxUserInfo) {
            val firstSubscribe: Pair<Boolean, Long> = userService.userSubscribe(wxUserInfo)
            if (firstSubscribe.first && firstSubscribe.second > 0L) {
                //首次关注送优惠券
                couponService.presentedCouponToUser(firstSubscribe.second, CouponConfigAction.FirstFollow)
            }
        }
    }

    /**
     * 用户取消关注公众号
     */
    private fun userUnSubscribe(openId: String) {
        val wxUserInfo: UserFromServerResult? = UserFromServerApi(openId).execute()
        if (null != wxUserInfo) {
            userService.userUnSubscribe(wxUserInfo)
        }
    }
}