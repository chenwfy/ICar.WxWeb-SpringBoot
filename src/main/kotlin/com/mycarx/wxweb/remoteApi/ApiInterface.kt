package com.mycarx.wxweb.remoteApi

interface ApiInterface<T> {
    fun execute() : T
}