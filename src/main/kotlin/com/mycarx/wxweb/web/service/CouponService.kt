package com.mycarx.wxweb.web.service

import com.mycarx.utils.StringUtils
import com.mycarx.utils.addSeconds
import com.mycarx.utils.convertToDate
import com.mycarx.utils.format
import com.mycarx.wxweb.web.domain.coupon.*
import com.mycarx.wxweb.web.repository.coupon.OilCouponConfigRepository
import com.mycarx.wxweb.web.repository.coupon.UserOilCouponRepository
import com.mycarx.wxweb.web.repository.coupon.UserWashCouponRepository
import com.mycarx.wxweb.web.repository.coupon.WashCouponConfigRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import kotlin.concurrent.thread

/**
 * 赠送优惠券相关服务类
 */
@Service
class CouponService {
    @Autowired
    private lateinit var oilCouponConfigRepository: OilCouponConfigRepository
    @Autowired
    private lateinit var userOilCouponRepository: UserOilCouponRepository
    @Autowired
    private lateinit var washCouponConfigRepository: WashCouponConfigRepository
    @Autowired
    private lateinit var userWashCouponRepository: UserWashCouponRepository

    /**
     * 向用户赠送优惠券
     */
    fun presentedCouponToUser(userId: Long, action: CouponConfigAction) {
        thread(start = true) {
            this.presentedCouponToUserBackWorker(userId, action)
        }
    }

    /**
     * 向用户赠送优惠券
     */
    private fun presentedCouponToUserBackWorker(userId: Long, action: CouponConfigAction) {
        this.presentedOilCouponToUserBackWorker(userId, action)
        this.presentedWashCouponToUserBackWorker(userId, action)
    }

    /**
     * 向用户赠送加油券
     */
    private fun presentedOilCouponToUserBackWorker(userId: Long, action: CouponConfigAction) {
        val dateNow = Date()
        try {
            val config: OilCouponConfigEntity = oilCouponConfigRepository.queryFistConfigByAction(action.name, dateNow) ?: return
            if (config.number > 0 && config.amount > BigDecimal.ZERO && config.timeLength > 0) {
                val couponValue: BigDecimal = config.amount
                val oilCoupon = UserOilCouponEntity(
                        userId,
                        StringUtils.getRandomLetters(),
                        couponValue,
                        config.useLimit,
                        dateNow.addSeconds(config.timeLength),
                        CouponState.NotUse,
                        config.id,
                        dateNow
                )

                oilCoupon.createdAt = "${dateNow.format("yyyy-MM-dd")} 00:00:00".convertToDate("yyyy-MM-dd HH:mm:ss")!!
                oilCoupon.expiredDate = "${dateNow.addSeconds(config.timeLength).format("yyyy-MM-dd")} 23:59:59".convertToDate("yyyy-MM-dd HH:mm:ss")!!

                userOilCouponRepository.save(oilCoupon)

                //这里还需要调用微信模板消息，通知给用户
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 向用户赠送洗车券
     */
    private fun presentedWashCouponToUserBackWorker(userId: Long, action: CouponConfigAction) {
        val dateNow = Date()
        try {
            val config: WashCouponConfigEntity = washCouponConfigRepository.queryFistConfigByAction(action.name, dateNow) ?: return
            if (config.amount > 0) {
                val washCoupon = UserWashCouponEntity(
                        userId,
                        StringUtils.getRandomLetters(),
                        dateNow.addSeconds(config.timeLength),
                        CouponState.NotUse,
                        config.id,
                        dateNow
                )
                washCoupon.createdAt = "${dateNow.format("yyyy-MM-dd")} 00:00:00".convertToDate("yyyy-MM-dd HH:mm:ss")!!
                washCoupon.expiredDate = "${dateNow.addSeconds(config.timeLength).format("yyyy-MM-dd")} 23:59:59".convertToDate("yyyy-MM-dd HH:mm:ss")!!

                userWashCouponRepository.save(washCoupon)

                //这里还需要调用微信模板消息，通知给用户
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
