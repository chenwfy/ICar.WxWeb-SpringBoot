package com.mycarx.weiXinPay

import com.github.wxpay.sdk.WXPayConfig
import com.mycarx.wxweb.config.AppConfig
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * 微信支付配置
 */
class WeiXinPayConfig : WXPayConfig {
    /**
     * 微信支付分配的公众账号ID（企业号corpId即为此appId）
     */
    override fun getAppID(): String {
        return AppConfig.weChatAppId
    }

    /**
     * 微信支付分配的商户号
     */
    override fun getMchID(): String {
        return AppConfig.weChatPayMchId
    }

    /**
     * API 密钥
     */
    override fun getKey(): String {
        return AppConfig.weChatPayKey
    }

    /**
     * 证书文件数据流
     */
    override fun getCertStream(): InputStream {
        return ByteArrayInputStream(byteArrayOf())
    }

    /**
     * 支付API请求超时
     */
    override fun getHttpConnectTimeoutMs(): Int {
        return 8000
    }

    /**
     * 支付API响应结果读取超时
     */
    override fun getHttpReadTimeoutMs(): Int {
        return 10000
    }
}