package com.mycarx.wxweb.remoteApi

abstract class ApiBase(method: QueryMethod = QueryMethod.GET) {
    val method: QueryMethod = method
    var url: String = ""
    var dataString: String = ""
    var dataMap : Map<Any, Any?> = emptyMap()
}