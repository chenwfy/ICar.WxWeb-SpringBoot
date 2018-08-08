package com.mycarx.wxweb.interceptor

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 用户授权认证处理
 */
class UserAuthInterceptor(interceptor: UserAuthInterceptorInterface) : HandlerInterceptorAdapter() {
    private val userAuthInterceptor: UserAuthInterceptorInterface = interceptor

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return this.userAuthInterceptor.doFilter(request, response)
    }
}