package com.mycarx.wxweb.web.service

import com.mycarx.utils.convertToDate
import com.mycarx.utils.format
import com.mycarx.weiXinPay.ProductType
import com.mycarx.weiXinPay.WeiXinPayResult
import com.mycarx.weiXinPay.WeiXinPayService
import com.mycarx.weiXinPay.WeiXinUnifiedOrder
import com.mycarx.wxweb.web.domain.common.*
import com.mycarx.wxweb.web.domain.oil.*
import com.mycarx.wxweb.web.domain.user.AuthUser
import com.mycarx.wxweb.web.repository.oil.*
import com.mycarx.wxweb.web.repository.JdbcTemplateRepository
import com.mycarx.wxweb.web.repository.coupon.UserOilCouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 加油相关业务服务类
 */
@Service
class OilService {
    @Autowired
    private lateinit var oilBrandRepository: OilBrandRepository
    @Autowired
    private lateinit var oilGunRepository: OilGunRepository
    @Autowired
    private lateinit var utilsService: UtilsService
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var oilPaymentOrderRepository: OilPaymentOrderRepository
    @Autowired
    private lateinit var userOilCouponRepository: UserOilCouponRepository
    @Autowired
    private lateinit var jdbcTemplateRepository: JdbcTemplateRepository

    /**
     * 获取加油站列表过滤条件
     */
    fun buildOilStationFilter(): OilStationFilter {
        val result = OilStationFilter()
        result.oilLabels.addAll(this.buildOilTypeFilters())
        result.oilBrands.addAll(this.buildOilBrandFilters())
        result.rangeBands.addAll(UtilsService.buildRangeBands())
        result.statusOptions.addAll(this.buildStationStatus())
        result.cityOptions.addAll(utilsService.buildStationCity(StationType.GasStation))
        return result
    }

    /**
     * 组装燃油类型（油标）筛选项
     */
    private fun buildOilTypeFilters(): List<KVPair<Long, String>> {
        val resultList = mutableListOf<KVPair<Long, String>>()
        this.queryAllOilTypeList(OilType.Gasoline).sortedBy { it.sort }.forEach { resultList.add(KVPair(it.id, it.oilsName)) }
        return resultList
    }

    /**
     * 组装燃油品牌筛选项
     */
    private fun buildOilBrandFilters(): List<KVPair<Long, String>> {
        val resultList = mutableListOf<KVPair<Long, String>>(KVPair(0, "不限品牌"))
        this.queryAllOilBrandList().forEach { resultList.add(KVPair(it.id, it.brandName)) }
        return resultList
    }

    /**
     * 组装加油站状态筛选选项
     */
    private fun buildStationStatus(): List<KVPair<String, String>> {
        val resultList = mutableListOf(KVPair("", "不限"))
        OilStationStatus.values().forEach { resultList.add(KVPair(it.name, it.label)) }
        return resultList
    }

    /**
     * 获取所有燃油品牌数据
     */
    fun queryAllOilBrandList(): List<OilBrandEntity> {
        return oilBrandRepository.findAll()
    }

    /**
     * 获取所有燃油类型（标号）数据
     */
    fun queryAllOilTypeList(oilType: OilType = OilType.All): List<OilTypeModel> {
        val oilTypeList: List<OilTypeModel> = jdbcTemplateRepository.findOilTypeListForOilStationListFilter()
        if (oilTypeList.isNotEmpty() && oilType != OilType.All) {
            return oilTypeList.filter { it -> it.kind.equals(oilType.name, true) }
        }
        return oilTypeList
    }

    /**
     * 查询符合筛选条件的加油站数据列表
     */
    fun queryOilStationListByFilter(filter: OilStationListFilter, size: Int = 0): List<OilStationForList> {
        return jdbcTemplateRepository.queryOilStationListByFilter(filter, size)
    }

    /**
     * 查询指定的加油站基本信息
     */
    fun queryOilStationBaseInfo(stationId: Long): OilStation? {
        return jdbcTemplateRepository.queryOilStationBaseInfo(stationId)
    }

    /**
     * 查询指定加油站所有油品(附带价格)信息列表
     */
    fun queryOilTypeListByStationId(stationId: Long, oilType: OilType = OilType.All): List<OilTypeWithPriceModel> {
        val oilTypeList: List<OilTypeWithPriceModel> = jdbcTemplateRepository.queryOilTypeListByStationId(stationId)
        if (oilTypeList.isNotEmpty() && oilType != OilType.All) {
            return oilTypeList.filter { it -> it.kind.equals(oilType.name, true) }
        }
        return oilTypeList
    }

    /**
     * 查询指定加油站、指定油品类型的所有油枪信息列表
     */
    fun queryGunListByStationIdAndOilTypeId(stationId: Long, typeId: Long): List<OilGunEntity> {
        return oilGunRepository.queryGunListByStationIdAndOilTypeId(stationId, typeId)
    }

    /**
     * 保存加油支付订单
     */
    fun saveOilPaymentOrder(paymentOrder: OilPaymentOrderEntity): Boolean {
        try {
            oilPaymentOrderRepository.save(paymentOrder)
            userService.updateUserOilTypeAndBrand(paymentOrder.userId, paymentOrder.oilTypeId, paymentOrder.oilBrandId)
            return true
        } catch (e: Exception) {

        }
        return false
    }

    /**
     * 发送微信支付请求
     */
    fun sendOilWeiXinPaymentRequest(userAuth: AuthUser, orderNo: String, priceTotal: Int, remoteId: String): WeiXinPayResult {
        val requestOrder = WeiXinUnifiedOrder(ProductType.Gas)
        requestOrder.openId = userAuth.openId
        requestOrder.orderNo = orderNo
        requestOrder.priceAmount = priceTotal
        requestOrder.remoteIp = remoteId

        return WeiXinPayService.payForUnifiedOrder(requestOrder)
    }

    /**
     * 查询指定的加油支付订单
     */
    fun findOilWeiXinPaymentOrderByOrderNo(orderNo: String): OilPaymentOrderEntity? {
        return oilPaymentOrderRepository.findOilWeiXinPaymentOrderByOrderNo(orderNo)
    }

    /**
     * 更新加油支付订单为已支付状态：
     * 1：更新 已支付状态、支付时间
     * 2：更新 订单每日序号
     * 3：如果订单使用了优惠券，则同步更新优惠券为已使用状态
     */
    @Transactional(rollbackFor = [Exception::class])
    fun updateOilWeiXinPaymentOrderPayStatus(order: OilPaymentOrderEntity): Boolean {
        val dateNow = Date()
        val todayStartTime: Date = ("${dateNow.format("yyyy-MM-dd")} 00:00:00").convertToDate("yyyy-MM-dd HH:mm:ss") ?: dateNow
        val orderNum: Int = oilPaymentOrderRepository.findMaxOrderNumAfterStartTime(order.storeId, todayStartTime) + 1
        try {
            oilPaymentOrderRepository.updateOilWeiXinPaymentOrderPayStatus(order.id, orderNum, dateNow)

            //是否有优惠券
            if (order.couponId > 0) {
                userOilCouponRepository.updateOilCouponStatus(order.couponId, order.userId)
            }

            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 更新加油支付订单针对小程序的下发通知状态
     */
    fun updateOilWeiXinPaymentOrderNotifyStatus(order: OilPaymentOrderEntity) {
        try {
            oilPaymentOrderRepository.updateOilWeiXinPaymentOrderNotifyStatus(order.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}