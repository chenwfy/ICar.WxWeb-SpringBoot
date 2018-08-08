package com.mycarx.wxweb.web.domain.common

/**
 * 订单支付方式枚举
 */
enum class OrderPayType {
    /**
     * 微信支付（用户主动发起支付）
     */
    WeiXin,
    /**
     * 微信支付（收款方发起支付）
     */
    WXCash,
    /**
     * 优惠券支付
     */
    Coupon,
    /**
     * 挂账支付
     */
    Account
}