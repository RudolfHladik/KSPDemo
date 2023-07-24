package com.rudolfhladik.kspdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform