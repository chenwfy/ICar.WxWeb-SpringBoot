package com.mycarx.wxweb.remoteApi.baiduTTS

import com.mycarx.wxweb.web.domain.baiduTTS.TTSTokenResult
import java.util.*

object TTSToken {
    private var accessToken: String = ""
    private var expiresIn: Int = 0
    private var expiration: Long = 0

    var value: String = ""
        get() {
            return this.get()
        }

    private fun get(): String {
        //提前2分钟过期
        val valid = this.accessToken.isNotBlank() && (Date().time + 2 * 60 * 1000) < this.expiration
        if (!valid) {
            val tokenResult: TTSTokenResult? = TTSTokenApi().execute()
            if (null != tokenResult) {
                this.accessToken = tokenResult.accessToken
                this.expiresIn = tokenResult.expiresIn
                this.expiration = Date().time + this.expiresIn * 1000
            }
        }

        return this.accessToken
    }
}