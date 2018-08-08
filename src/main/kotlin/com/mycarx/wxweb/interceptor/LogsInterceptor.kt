package com.mycarx.wxweb.interceptor

import com.mycarx.utils.LogUtils
import com.mycarx.utils.encryptMd5
import com.mycarx.utils.getRemoteIp
import com.mycarx.utils.jsonSerialize
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogsInterceptor : HandlerInterceptorAdapter() {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        //记录请求日志
        try {
            val requestMethod = request?.method ?: ""
            val requestUri = request?.requestURL ?: ""
            val remoteIp = request.getRemoteIp()
            val sessionId = request?.session?.id ?: ""
            val requestData = request?.parameterMap?.jsonSerialize() ?: ""
            val requestTime = Date().time
            val uuid = "$requestMethod$requestUri$remoteIp$sessionId$requestData$requestTime".encryptMd5(16)
            request?.setAttribute("RequestUUID", uuid)
            request?.setAttribute("StartTime", requestTime)
            LogUtils.logger.info("[$requestMethod][$uuid] START：======> [$requestUri][$remoteIp][$sessionId]：$requestData")
        } catch (e: Exception) {
            LogUtils.logger.info(e.message)
            e.printStackTrace()
        }

        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: java.lang.Exception?) {
        try {
            if (null != ex) {
                LogUtils.logger.info(ex.message)
                ex.printStackTrace()
            }

            val status: Int = response?.status ?: 500
            val requestMethod = request?.method ?: ""
            val requestUUID = request?.getAttribute("RequestUUID")?.toString() ?: ""
            val startTime: Long = (request?.getAttribute("StartTime")?.toString() ?: "0").toLong()
            val dateNow = Date().time
            val usedTime = dateNow - startTime
            LogUtils.logger.info("[$requestMethod][$requestUUID] END：======> [用时：$usedTime][$status]")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}