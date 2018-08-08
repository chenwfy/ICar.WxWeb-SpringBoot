package com.mycarx.wxweb.web.domain

class RestResultData<T>(data: T?) : RestResultBase() {
    constructor() : this(null)

    var data: T? = data
}