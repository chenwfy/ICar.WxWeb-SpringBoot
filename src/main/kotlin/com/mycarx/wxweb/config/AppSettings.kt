package com.mycarx.wxweb.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "settings")
class AppSettings {
    var environment: String = ""
    var webHost: String = ""
    var staticFileUrlPrefix: String = ""
    var staticFileSaveRoot: String = ""
    var weChatAppId: String = ""
    var weChatAppSecret: String = ""
    var weChatPayMchId: String = ""
    var weChatPayKey: String = ""
    var weChatPayNotifyUrl: String = ""
    var weChatWebName: String = ""
    var weChatMessageToken: String = ""
    var weChatMessageAESKey: String = ""
    var weChatAppPushHost: String = ""
    var smsAppKey: String = ""
    var smsAppSecret: String = ""
    var smsAppSign: String = ""
    var smsTemplateId: String = ""
    var smsCodeName: String = ""
    var smsValidDuration: Int = 600
    var smsMaxTimesInDuration: Int = 10
    var baiDuMapAppKey: String = ""
    var baiDuTTSAppId: String = ""
    var baiDuTTSAppKey: String = ""
    var baiDuTTSAppSecret: String = ""
    var shareTitle: String = ""
    var shareDesc: String = ""
    var shareUrl: String = ""
    var shareIcon: String = ""
    var shareQrCodeImg: String = ""
}