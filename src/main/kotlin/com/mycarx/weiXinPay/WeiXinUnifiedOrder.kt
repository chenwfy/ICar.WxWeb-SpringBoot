package com.mycarx.weiXinPay

import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.web.domain.JsonSerializable

/**
 * 微信支付统一下单所需信息
 */
class WeiXinUnifiedOrder(productType: ProductType = ProductType.Gas) : JsonSerializable() {
    private var productionType: ProductType = productType

    /**
     * 订单号
     */
    var orderNo: String = ""
    /**
     * 商品名称
     */
    val productionName: String = "${AppConfig.weChatWebName}-${productionType.typeName}"
    /**
     * 订单价格，单位：分
     */
    var priceAmount: Int = 0
    /**
     * 付款人openId
     */
    var openId: String = ""
    /**
     * 付款人IP地址
     */
    var remoteIp: String = ""
}