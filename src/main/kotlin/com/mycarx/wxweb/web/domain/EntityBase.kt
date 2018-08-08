package com.mycarx.wxweb.web.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * 数据表实体基类
 */
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
open class EntityBase : JsonSerializable() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    var createdAt: Date = Date()
}