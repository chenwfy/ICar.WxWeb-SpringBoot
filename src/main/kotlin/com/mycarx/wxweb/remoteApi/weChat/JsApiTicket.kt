package com.mycarx.wxweb.remoteApi.weChat

import com.mycarx.wxweb.web.domain.weChat.JsApiTicketResult
import java.util.*

object JsApiTicket {
    private var ticket: String = ""
    private var expiresIn: Int = 0
    private var expiration: Long = 0

    var value: String = ""
        get() {
            return this.get()
        }

    private fun get(): String {
        //提前2分钟过期
        val valid = this.ticket.isNotBlank() && (Date().time + 2 * 60 * 1000) < this.expiration
        if (!valid) {
            val result: JsApiTicketResult? = JsApiTicketApi().execute()
            if (null != result) {
                this.ticket = result.ticket
                this.expiresIn = result.Expired
                this.expiration = Date().time + this.expiresIn * 1000
            }
        }

        return this.ticket
    }
}