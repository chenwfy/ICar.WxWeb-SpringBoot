package com.mycarx.wxweb.config

object AppConfig {
    private var environmentName: String = ""
    var environment: Environment = Environment.DEV
        private set
        get() {
            return Environment.valueOf(this.environmentName)
        }

    var webHost: String = ""
        private set

    var weChatAppId: String = ""
        private set

    var weChatAppSecret: String = ""
        private set

    var weChatPayMchId: String = ""
        private set

    var weChatPayKey: String = ""
        private set

    var weChatPayNotifyUrl: String = ""
        private set

    var weChatWebName: String = ""
        private set

    var baiDuMapAppKey: String = ""
        private set

    var baiDuTTSAppId: String = ""
        private set

    var baiDuTTSAppKey: String = ""
        private set

    var baiDuTTSAppSecret: String = ""
        private set

    var smsValidDuration: Int = 600
        private set

    var smsMaxTimesInDuration: Int = 10
        private set

    var weChatAppPushHost: String = ""
        private set

    var staticFileUrlPrefix: String = ""
        private set

    var staticFileSaveRoot: String = ""
        private set

    var weChatMessageToken: String = ""
        private set

    var weChatMessageAESKey: String = ""
        private set

    var smsAppKey: String = ""
        private set

    var smsAppSecret: String = ""
        private set

    var smsAppSign: String = ""
        private set

    var smsTemplateId: String = ""
        private set

    var smsCodeName: String = ""
        private set

    var shareTitle: String = ""
        private set

    var shareDesc: String = ""
        private set

    var shareUrl: String = ""
        private set

    var shareIcon: String = ""
        private set

    var shareQrCodeImg: String = ""
        private set

    fun set(settings: AppSettings) {
        this.environmentName = settings.environment
        this.webHost = settings.webHost
        this.staticFileUrlPrefix = settings.staticFileUrlPrefix
        this.staticFileSaveRoot = settings.staticFileSaveRoot
        this.weChatAppId = settings.weChatAppId
        this.weChatAppSecret = settings.weChatAppSecret
        this.weChatPayMchId = settings.weChatPayMchId
        this.weChatPayKey = settings.weChatPayKey
        this.weChatPayNotifyUrl = settings.weChatPayNotifyUrl
        this.weChatWebName = settings.weChatWebName
        this.weChatMessageToken = settings.weChatMessageToken
        this.weChatMessageAESKey = settings.weChatMessageAESKey
        this.weChatAppPushHost = settings.weChatAppPushHost
        this.smsAppKey = settings.smsAppKey
        this.smsAppSecret = settings.smsAppSecret
        this.smsAppSign = settings.smsAppSign
        this.smsTemplateId = settings.smsTemplateId
        this.smsCodeName = settings.smsCodeName
        this.smsValidDuration = settings.smsValidDuration
        this.smsMaxTimesInDuration = settings.smsMaxTimesInDuration
        this.baiDuMapAppKey = settings.baiDuMapAppKey
        this.baiDuTTSAppId = settings.baiDuTTSAppId
        this.baiDuTTSAppKey = settings.baiDuTTSAppKey
        this.baiDuTTSAppSecret = settings.baiDuTTSAppSecret
        this.shareTitle = settings.shareTitle
        this.shareDesc = settings.shareDesc
        this.shareUrl = settings.shareUrl
        this.shareIcon = settings.shareIcon
        this.shareQrCodeImg = settings.shareQrCodeImg
    }
}