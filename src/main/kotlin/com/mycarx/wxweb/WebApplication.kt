package com.mycarx.wxweb

import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.config.AppSettings
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class WebApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val context = SpringApplication.run(WebApplication::class.java, *args)

            val settings: AppSettings = context.getBean(AppSettings::class.java)
            AppConfig.set(settings)
        }
    }
}