package com.mycarx.wxweb.web.domain

import kotlin.reflect.full.memberProperties

open class RequestBase : JsonSerializable(), MapAbleInterface {
    override fun toMap(): Map<Any, Any?> {
        var map = mutableMapOf<Any, Any?>()
        this.javaClass.kotlin.memberProperties.forEach {
            map[it.name] = it.get(this)
        }

        return map
    }
}