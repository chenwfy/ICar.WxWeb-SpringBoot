package com.mycarx.weiXinPay

import java.util.HashMap
import com.github.wxpay.sdk.WXPay
import com.github.wxpay.sdk.WXPayConfig
import com.mycarx.wxweb.config.AppConfig

/**
 * 微信支付
 */
object WeiXinPayService {
    /**
     * return_code 	SUCCESS/FAIL   此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断返回信息
     * return_msg 	返回信息，如非空，为错误原因
     *
     * ====以下字段在return_code为SUCCESS的时候有返回====
     *
     * 公众账号ID          appid                   调用接口提交的公众账号ID
     * 商户号              mch_id                 调用接口提交的商户号
     * 设备号              device_info            自定义参数，可以为请求支付的终端设备号等
     * 随机字符串           nonce_str               微信返回的随机字符串（32位以内）
     * 签名                sign                  微信返回的签名值，详见签名算法
     * 业务结果            result_code             SUCCESS/FAIL
     * 错误代码 	        err_code                详细参见下文错误列表
     * 错误代码描述          err_code_des            错误信息描述
     *
     * ====以下字段在return_code 和result_code都为SUCCESS的时候有返回 ====
     *
     * 交易类型 	            trade_type              JSAPI 公众号支付 NATIVE 扫码支付     APP APP支付
     * 预支付交易会话标识    	prepay_id               微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     * 二维码链接 	        code_url                trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
     */

    /**
     * 调用微信统一下单接口进行支付
     */
    fun payForUnifiedOrder(order: WeiXinUnifiedOrder): WeiXinPayResult {
        val config: WXPayConfig = WeiXinPayConfig()
        val payService = WXPay(config)

        //支付参数MAP
        val paymentMap = HashMap<String, String>()

        //自定义参数
        paymentMap["device_info"] = "WEB"

        //货币单位
        paymentMap["fee_type"] = "CNY"

        //支付回调通知地址
        paymentMap["notify_url"] = AppConfig.weChatPayNotifyUrl

        //支付调起方式：JSAPI - 公众号支付；NATIVE - 扫码支付；APP - APP支付
        paymentMap["trade_type"] = "JSAPI"

        //商品名称
        paymentMap["body"] = order.productionName

        //订单编号
        paymentMap["out_trade_no"] = order.orderNo

        //支付价格，单位：分
        paymentMap["total_fee"] = order.priceAmount.toString()

        //付款用户openId
        paymentMap["openid"] = order.openId

        //付款用户IP地址
        paymentMap["spbill_create_ip"] = order.remoteIp


        try {
            val result: Map<String, String> = payService.unifiedOrder(paymentMap)
            return payResultMapToWeiXinPayResult(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return WeiXinPayResult()
    }

    private fun payResultMapToWeiXinPayResult(resultMap: Map<String, String>): WeiXinPayResult {
        val result = WeiXinPayResult()
        if (resultMap.isNotEmpty()) {
            result.returnCode = (resultMap["return_code"] ?: "FAIL").toUpperCase()
            result.returnMessage = resultMap["return_msg"] ?: ""

            if (result.returnCode.equals("SUCCESS")) {
                result.appId = resultMap["appid"] ?: ""
                result.mchId = resultMap["mch_id"] ?: ""
                result.deviceInfo = resultMap["device_info"] ?: ""
                result.nonceStr = resultMap["nonce_str"] ?: ""
                result.sign = resultMap["sign"] ?: ""
                result.resultCode = (resultMap["result_code"] ?: "FAIL").toUpperCase()
                result.errorCode = resultMap["err_code"] ?: ""
                result.errorMessage = resultMap["err_code_des"] ?: ""

                if (result.resultCode.equals("SUCCESS")) {
                    result.tradeType = resultMap["trade_type"] ?: ""
                    result.prepayId = resultMap["prepay_id"] ?: ""
                }
            }
        }

        return result
    }
}