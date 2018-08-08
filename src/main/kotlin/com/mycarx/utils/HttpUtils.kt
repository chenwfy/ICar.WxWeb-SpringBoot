package com.mycarx.utils

import com.mycarx.wxweb.web.domain.RestResult
import com.mycarx.wxweb.web.domain.RestResultBase
import javax.servlet.http.HttpServletResponse

/**
 * HTTP请求相关辅助类
 */
class HttpUtils {
    companion object {
        @JvmStatic
        fun renderSuccess(response: HttpServletResponse) {
            val result: RestResult = RestResultBase()
            renderRestResult(response, result)
        }

        @JvmStatic
        fun renderError(response: HttpServletResponse, errorReason: String) {
            val result: RestResult = RestResultBase(errorReason)
            renderRestResult(response, result)
        }

        @JvmStatic
        fun renderAuthExpired(response: HttpServletResponse) {
            val result: RestResult = RestResultBase(1001, "您尚未登录或登录已过期！")
            renderRestResult(response, result)
        }

        @JvmStatic
        fun renderRestResult(response: HttpServletResponse, respond: RestResult) {
            response.setJsonHeader().end(respond.jsonSerialize())
        }
    }
}