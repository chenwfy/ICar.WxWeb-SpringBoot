package com.mycarx.wxweb.web.domain

import kotlin.reflect.full.memberProperties

abstract class MapAble : JsonSerializable(), MapAbleInterface {
    override fun toMap(): Map<Any, Any?> {
        var map = mutableMapOf<Any, Any?>()
        this.javaClass.kotlin.memberProperties.forEach {
            map.put(it.name, it.get(this))
        }

        return map
    }
}