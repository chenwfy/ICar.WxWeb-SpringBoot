package com.mycarx.utils

import com.mycarx.wxweb.web.domain.user.AuthUser
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpSession

class SessionUtils {
    companion object {
        @JvmStatic
        private val authSessionKey: String = "MYCARX.CURRENT.USER"

        /**
         * 获取当前SESSION实例
         */
        @JvmStatic
        fun getSessionContext(): HttpSession? {
            try {
                val requestAttrs = RequestContextHolder.getRequestAttributes()
                val request = (requestAttrs as ServletRequestAttributes).request
                return request.session
            } catch (e: Exception) {
                LogUtils.logger.info(e.message)
            }
            return null
        }

        @JvmStatic
        fun setAuthSession(sessionContext: HttpSession?, sessionValue: AuthUser) {
            if (null != sessionContext) {
                sessionContext.setSession(authSessionKey, sessionValue)
            }
        }

        @JvmStatic
        fun getAuthSession(sessionContext: HttpSession?): AuthUser? {
            if (null != sessionContext) {
                var session = sessionContext.getSession(authSessionKey)
                if (null != session) {
                    return session as AuthUser
                }
            }
            return null
        }
    }
}