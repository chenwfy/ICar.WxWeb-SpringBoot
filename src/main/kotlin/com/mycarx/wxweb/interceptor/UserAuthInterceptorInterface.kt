package com.mycarx.wxweb.interceptor

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface UserAuthInterceptorInterface {
    fun doFilter(request: HttpServletRequest, response: HttpServletResponse) : Boolean
}