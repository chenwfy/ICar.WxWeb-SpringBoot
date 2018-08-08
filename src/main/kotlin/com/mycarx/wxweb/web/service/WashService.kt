package com.mycarx.wxweb.web.service

import com.mycarx.utils.convertToDate
import com.mycarx.utils.format
import com.mycarx.wxweb.web.domain.common.KVPair
import com.mycarx.wxweb.web.domain.common.StationType
import com.mycarx.wxweb.web.domain.wash.*
import com.mycarx.wxweb.web.repository.JdbcTemplateRepository
import com.mycarx.wxweb.web.repository.coupon.UserWashCouponRepository
import com.mycarx.wxweb.web.repository.wash.WashMachineRepository
import com.mycarx.wxweb.web.repository.wash.WashPaymentOrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 洗车业务逻辑服务
 */
@Service
class WashService {
    @Autowired
    private lateinit var utilsService: UtilsService
    @Autowired
    private lateinit var washMachineRepository: WashMachineRepository
    @Autowired
    private lateinit var washPaymentOrderRepository: WashPaymentOrderRepository
    @Autowired
    private lateinit var userWashCouponRepository: UserWashCouponRepository
    @Autowired
    private lateinit var jdbcTemplateRepository: JdbcTemplateRepository

    /**
     * 获取洗车门店列表过滤条件
     */
    fun buildWashStationFilter(): WashStationFilter {
        val result = WashStationFilter()
        result.rangeBands.addAll(UtilsService.buildRangeBands())
        result.statusOptions.addAll(this.buildStationStatus())
        result.cityOptions.addAll(utilsService.buildStationCity(StationType.CarWashShop))
        return result
    }

    /**
     * 组装洗车门店状态筛选选项
     */
    private fun buildStationStatus(): List<KVPair<String, String>> {
        val resultList = mutableListOf(KVPair("", "不限"))
        WashStationStatus.values().forEach { resultList.add(KVPair(it.name, it.label)) }
        return resultList
    }

    /**
     * 查询符合筛选条件的洗车门店数据列表
     */
    fun queryWashStationListByFilter(filter: WashStationListFilter, size: Int = 0): List<WashStation> {
        return jdbcTemplateRepository.queryWashStationListByFilter(filter, size)
    }

    /**
     * 查询指定的洗车门店基本信息
     */
    fun queryWashStationBaseInfo(stationId: Long): WashStation? {
        return jdbcTemplateRepository.queryWashStationBaseInfo(stationId)
    }

    /**
     * 由洗车门店和洗车机编号查询洗车机信息
     */
    fun queryWashMachineByStationIdAndNumber(stationId: Long, machineId: Long): WashMachineEntity? {
        return washMachineRepository.queryByStoreIdAndMachineNumber(stationId, machineId)
    }

    /**
     * 创建洗车支付订单
     */
    fun saveWashPaymentOrder(paymentOrder: WashPaymentOrderEntity): Boolean {
        try {
            washPaymentOrderRepository.save(paymentOrder)
            return true
        } catch (e: Exception) {

        }
        return false
    }

    /**
     * 查询指定的洗车支付订单
     */
    fun findWashPaymentOrderByOrderNo(orderNo: String): WashPaymentOrderEntity? {
        return washPaymentOrderRepository.findWashPaymentOrderByOrderNo(orderNo)
    }

    /**
     * 查询当前用户当前月挂账洗车次数
     */
    fun findWashOrderCountByUserOnCredit(userId: Long): Int {
        val dateNow = Date()
        val startTime: Date = ("${dateNow.format("yyyy-MM-01")} 00:00:00").convertToDate("yyyy-MM-dd HH:mm:ss") ?: dateNow
        return washPaymentOrderRepository.findWashOrderCountByUserOnCredit(userId, startTime)
    }

    /**
     * 更新有人值守洗车，使用洗车券或者挂账洗车的支付订单相关状态：
     * 更新字段包括：洗车机号、洗车机所属分组号、收银员、已支付状态、支付时间、通知状态、订单每日序号
     * 如果订单使用了优惠券，则同步更新优惠券为已使用状态
     */
    @Transactional(rollbackFor = [Exception::class])
    fun updateWashOtherPayTypeOrderPayStatus(order: WashPaymentOrderEntity): Boolean {
        val dateNow = Date()
        val todayStartTime: Date = ("${dateNow.format("yyyy-MM-dd")} 00:00:00").convertToDate("yyyy-MM-dd HH:mm:ss") ?: dateNow
        val orderNum: Int = washPaymentOrderRepository.findMaxOrderNumAfterStartTime(order.storeId, todayStartTime)
        try {
            order.num = orderNum + 1
            washPaymentOrderRepository.save(order)

            //是否有优惠券
            if (order.couponId > 0) {
                userWashCouponRepository.updateWashCouponStatus(order.couponId, order.userId)
            }

            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 更新有人值守洗车，使用微信支付订单相关状态：
     * 更新 洗车机号、洗车机所属分组号、收银员
     */
    fun updateWashWeiXinPayTypeOrderPayee(order: WashPaymentOrderEntity): Boolean {
        try {
            washPaymentOrderRepository.save(order)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 更新有人值守洗车，使用微信支付订单相关状态：
     * 更新 支付时间、微信预付订单号
     */
    fun updateWashWeiXinPayTypeOrderWXOrderNo(order: WashPaymentOrderEntity): Boolean {
        try {
            washPaymentOrderRepository.updateWashWeiXinPayTypeOrderWXOrderNo(order.id, order.wxOrderNo, order.payDate)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 更新有人值守洗车，使用微信支付订单相关状态：
     * 更新 已支付状态、支付时间、订单每日序号
     */
    fun updateWashWeiXinPayTypeOrderPayStatus(order: WashPaymentOrderEntity): Boolean {
        val dateNow = Date()
        val todayStartTime: Date = ("${dateNow.format("yyyy-MM-dd")} 00:00:00").convertToDate("yyyy-MM-dd HH:mm:ss") ?: dateNow
        val orderNum: Int = washPaymentOrderRepository.findMaxOrderNumAfterStartTime(order.storeId, todayStartTime) + 1
        try {
            washPaymentOrderRepository.updateWashWeiXinPayTypeOrderPayStatus(order.id, orderNum, dateNow)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 更新洗车订单下发通知状态
     */
    fun updateWashPaymentOrderNotifyStatus(order: WashPaymentOrderEntity) {
        washPaymentOrderRepository.updateWashPaymentOrderNotifyStatus(order.id)
    }
}