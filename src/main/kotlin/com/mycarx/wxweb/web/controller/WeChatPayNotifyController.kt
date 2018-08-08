package com.mycarx.wxweb.web.controller


import com.mycarx.utils.*
import com.mycarx.wxweb.remoteApi.baiduTTS.BaiDuText2AudioApi
import com.mycarx.wxweb.remoteApi.wxAppPush.PushOilOrderNotifyApi
import com.mycarx.wxweb.remoteApi.wxAppPush.PushWashOrderNotifyApi
import com.mycarx.wxweb.web.domain.RestResultBase
import com.mycarx.wxweb.web.domain.common.OrderPayStatus
import com.mycarx.wxweb.web.domain.coupon.CouponConfigAction
import com.mycarx.wxweb.web.domain.oil.OilPaymentOrderEntity
import com.mycarx.wxweb.web.domain.oil.OilTypeModel
import com.mycarx.wxweb.web.domain.push.OilOrderNotifyRequest
import com.mycarx.wxweb.web.domain.push.WashOrderNotifyRequest
import com.mycarx.wxweb.web.domain.wash.WashPaymentOrderEntity
import com.mycarx.wxweb.web.service.CouponService
import com.mycarx.wxweb.web.service.OilService
import com.mycarx.wxweb.web.service.WashService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 微信支付通知回调相关接口
 */
@RestController
@RequestMapping("/wxPay")
class WeChatPayNotifyController {
    @Autowired
    private lateinit var oilService: OilService
    @Autowired
    private lateinit var washService: WashService
    @Autowired
    private lateinit var couponService: CouponService

    /**
     * 微信支付通知
     */
    @RequestMapping("/payNotify")
    fun weiXinPaymentNotify(request: HttpServletRequest, response: HttpServletResponse) {
        //val requestMap = readXmlToMap(request)
        val requestMap = request.readXmlAsMap()
        if (requestMap["return_code"] == "SUCCESS") {
            if (requestMap.containsKey("result_code") && requestMap["result_code"] == "SUCCESS") {
                val orderNo: String = if (requestMap.containsKey("out_trade_no")) requestMap["out_trade_no"] ?: "" else ""
                if (orderNo.isNotEmpty()) {
                    //加油支付
                    if (orderNo.startsWith("WOG", true)) {
                        this.oilPaymentNotify(orderNo)
                    }

                    //人工洗车支付
                    if (orderNo.startsWith("WOW", true)) {
                        this.washPaymentNotify(orderNo)
                    }

                    //无人值守洗车支付
                    if (orderNo.startsWith("WOM", true)) {
                        this.washPaymentForMachineNotify(orderNo)
                    }
                }
            }
        }

        //响应
        val resXml = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>"
        response.setHeader("content-type", "text/xml")
        response.end(resXml)
    }

    /**
     * 加油支付成功回调处理
     */
    private fun oilPaymentNotify(orderNo: String) {
        val orderInfo: OilPaymentOrderEntity = oilService.findOilWeiXinPaymentOrderByOrderNo(orderNo) ?: return
        //如果订单已支付，则直接返回
        if (orderInfo.payState == OrderPayStatus.Payed) {
            return
        }

        orderInfo.ttsFile = this.loadAndSaveOrderAudioFile(orderInfo)
        orderInfo.payState = OrderPayStatus.Payed
        val updated = oilService.updateOilWeiXinPaymentOrderPayStatus(orderInfo)

        if (updated) {
            val pushResult: RestResultBase? = PushOilOrderNotifyApi(OilOrderNotifyRequest(orderInfo.orderNo, orderInfo.storeId)).execute()
            if (null != pushResult && pushResult.code == 1) {
                oilService.updateOilWeiXinPaymentOrderNotifyStatus(orderInfo)
            }
        }
    }

    /**
     * 下载并保存订单播报的语音文件
     */
    private fun loadAndSaveOrderAudioFile(orderInfo: OilPaymentOrderEntity): String {
        val oilTypeList: List<OilTypeModel> = oilService.queryAllOilTypeList()
        val orderOilType: OilTypeModel? = oilTypeList.find { it -> it.id == orderInfo.oilTypeId }
        val oilTypeName: String = orderOilType?.oilsName ?: "油"
        val text = "${orderInfo.oilGun}号枪加$oilTypeName，已支付${orderInfo.actualAmount.divide(2)}元"
        val audioFileData: ByteArray = BaiDuText2AudioApi.loadAudioData(text)
        if (audioFileData.isNotEmpty()) {
            return FileUtils.saveOrderAudioFileAndCreateUrl(audioFileData)
        }
        return ""
    }

    /**
     * 洗车（人工收费方式 - 采用微信支付）支付成功回调处理
     */
    private fun washPaymentNotify(orderNo: String) {
        val orderInfo: WashPaymentOrderEntity = washService.findWashPaymentOrderByOrderNo(orderNo) ?: return
        //如果订单已支付，则直接返回
        if (orderInfo.payState == OrderPayStatus.Payed) {
            return
        }

        orderInfo.payState = OrderPayStatus.Payed
        val updated = washService.updateWashWeiXinPayTypeOrderPayStatus(orderInfo)
        if (updated) {
            //送加油券
            couponService.presentedCouponToUser(orderInfo.userId, CouponConfigAction.WCar)

            //推送支付成功消息给小程序
            val pushResult: RestResultBase? = PushWashOrderNotifyApi(WashOrderNotifyRequest(orderInfo.orderNo, orderInfo.storeId, orderInfo.cashierId)).execute()
            if (null != pushResult && pushResult.code == 1) {
                washService.updateWashPaymentOrderNotifyStatus(orderInfo)
            }
        }
    }

    /**
     * 洗车（无人值守模式 - 采用微信支付）支付成功回调处理
     */
    private fun washPaymentForMachineNotify(orderNo: String) {
        val orderInfo: WashPaymentOrderEntity = washService.findWashPaymentOrderByOrderNo(orderNo) ?: return
        //如果订单已支付，则直接返回
        if (orderInfo.payState == OrderPayStatus.Payed) {
            return
        }

        orderInfo.payState = OrderPayStatus.Payed
        val updated = washService.updateWashWeiXinPayTypeOrderPayStatus(orderInfo)
        if (updated) {
            //送加油券
            couponService.presentedCouponToUser(orderInfo.userId, CouponConfigAction.WCar)

            //推送支付成功消息给洗车机，通知洗车机开始工作
        }
    }
}