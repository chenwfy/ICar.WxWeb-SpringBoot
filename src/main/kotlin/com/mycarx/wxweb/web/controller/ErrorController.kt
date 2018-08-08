package com.mycarx.wxweb.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ErrorController {
    /**
     * 404
     */
    @GetMapping("/404")
    fun notFound(): String {
        return "404"
    }
}