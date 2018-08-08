package com.mycarx.wxweb.web.service

import com.mycarx.utils.SmsVerifyCodeUtils
import com.mycarx.utils.addMinutes
import com.mycarx.utils.addSeconds
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.web.domain.support.SmsVerifyCodeEntity
import com.mycarx.wxweb.web.repository.support.SmsVerifyCodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * 短信验证码服务
 */
@Service
class SmsVerifyCodeService {
    @Autowired
    private lateinit var smsVerifyCodeRepository: SmsVerifyCodeRepository

    /**
     * 查询指定手机号码在指定时间之后，发送短信的次数
     */
    fun querySmsCountAfterTime(mobile: String, startTime: Date): Int {
        return smsVerifyCodeRepository.querySmsCountAfterTime(mobile, startTime)
    }

    /**
     * 发送短信验证码
     */
    fun sendSmsVerifyCode(mobile: String): Boolean {
        val verifyCode: String = SmsVerifyCodeUtils.createVerifyCode()
        if (SmsVerifyCodeUtils.sendSms(mobile, verifyCode)) {
            val entity = SmsVerifyCodeEntity()
            entity.mobile = mobile
            entity.verifyCode = verifyCode
            entity.expiredDate = Date().addSeconds(AppConfig.smsValidDuration)
            smsVerifyCodeRepository.save(entity)

            return true
        }

        return false
    }

    /**
     * 验证短信验证码是否有效
     */
    fun validateSmsVerifyCode(mobile: String, verifyCode: String): Boolean {
        val entity: SmsVerifyCodeEntity? = smsVerifyCodeRepository.findLastByMobile(mobile)
        if (entity != null) {
            val timeNow = Date()
            if (entity.expiredDate > timeNow && entity.verificationDate == null && entity.verifyCode.equals(verifyCode, true)) {
                entity.verificationDate = timeNow
                smsVerifyCodeRepository.save(entity)

                return true
            }
        }
        return false
    }
}