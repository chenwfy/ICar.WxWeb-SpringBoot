package com.mycarx.utils

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest
import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.profile.DefaultProfile
import com.aliyuncs.profile.IClientProfile
import com.mycarx.wxweb.config.AppConfig

/**
 * 短信验证码相关辅助类
 */
class SmsVerifyCodeUtils {
    companion object {
        /**
         * 创建短信验证码
         */
        @JvmStatic
        fun createVerifyCode(): String {
            return StringUtils.getRandomNumbers()
        }

        /**
         * 发送短信
         * 阿里短信接口
         */
        @JvmStatic
        fun sendSms(mobileNumber: String, verifyCode: String): Boolean {
            val appKey = AppConfig.smsAppKey
            val appSecret = AppConfig.smsAppSecret
            val apiSign = AppConfig.smsAppSign
            val smsTemplateId = AppConfig.smsTemplateId
            val smsTemplateParams = "{\"${AppConfig.smsCodeName}\":\"$verifyCode\"}"
            val apiProductId = "Dysmsapi"
            val apiDomain = "dysmsapi.aliyuncs.com"
            val apiNodeName = "cn-hangzhou"
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000")
            System.setProperty("sun.net.client.defaultReadTimeout", "10000")

            //组装请求对象
            val request = SendSmsRequest()
            request.phoneNumbers = mobileNumber
            request.signName = apiSign
            request.templateCode = smsTemplateId
            request.templateParam = smsTemplateParams

            DefaultProfile.addEndpoint(apiNodeName, apiNodeName, apiProductId, apiDomain)
            val profile: IClientProfile = DefaultProfile.getProfile(apiNodeName, appKey, appSecret)

            try {
                //请求失败这里会抛ClientException异常
                val sendSmsResponse: SendSmsResponse? = DefaultAcsClient(profile).getAcsResponse(request)
                LogUtils.logger.info("发送短信结果：${sendSmsResponse?.jsonSerialize()}")

                val resultCode: String = sendSmsResponse?.code ?: ""
                return resultCode.equals("OK", true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}