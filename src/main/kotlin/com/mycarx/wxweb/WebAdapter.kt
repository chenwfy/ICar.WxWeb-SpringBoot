package com.mycarx.wxweb

import com.mycarx.wxweb.interceptor.BusinessUserAuthInterceptor
import com.mycarx.wxweb.interceptor.LogsInterceptor
import com.mycarx.wxweb.interceptor.UserAuthInterceptor
import com.mycarx.wxweb.interceptor.UserAuthInterceptorInterface
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
import org.springframework.boot.web.servlet.ErrorPage
import org.springframework.http.HttpStatus

@Configuration
class WebAdapter : WebMvcConfigurerAdapter() {
    @Bean
    fun businessUserAuthInterceptor(): UserAuthInterceptorInterface {
        return BusinessUserAuthInterceptor()
    }

    @Bean
    fun userAuthInterceptor(): UserAuthInterceptor {
        return UserAuthInterceptor(businessUserAuthInterceptor())
    }

    @Bean
    fun containerCustomizer(): EmbeddedServletContainerCustomizer {
        return EmbeddedServletContainerCustomizer { container ->
            container.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "/404"))
        }
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        //请求日志拦截器
        registry.addInterceptor(LogsInterceptor()).addPathPatterns("/**")
        //请求鉴权拦截器
        registry.addInterceptor(userAuthInterceptor())
                .excludePathPatterns("/wechat", "/wechat/**", "/wxPay/**", "/wxAppService/**", "/wxMessage", "/wxMessage/**")
                .excludePathPatterns("/common/**", "/other/**")
                .excludePathPatterns("/error", "/404")
                .addPathPatterns("/**")

        super.addInterceptors(registry)
    }
}