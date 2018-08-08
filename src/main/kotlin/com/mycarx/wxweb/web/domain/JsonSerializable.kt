package com.mycarx.wxweb.web.domain

import com.mycarx.utils.JsonUtils
import java.io.Serializable

/**
 * 可进行JSON序列化的实体抽象类
 */
abstract class JsonSerializable : Serializable {
    override fun toString(): String {
        return JsonUtils.serialize(this)
    }
}