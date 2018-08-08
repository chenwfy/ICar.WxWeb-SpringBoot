package com.mycarx.wxweb.web.domain

class RestResultPaging<T>(recordCount: Int = 0, pageSize: Int = 10, pageIndex: Int = 1) : RestResultBase() {
    val recordCount: Int = recordCount

    var pageSize: Int = pageSize
        set(value) {
            field = if (value < 1) 10 else value
        }

    var pageIndex: Int = pageIndex
        set(value) {
            field = if (value < 1) 1 else value
        }
        get() {
            return if (field >= pageCount) pageCount else field
        }

    var pageCount: Int = 1
        get() {
            var count = recordCount / pageSize
            if (recordCount % pageSize != 0) count++
            return if (count < 1) 1 else count
        }

    var data = mutableListOf<T>()
}