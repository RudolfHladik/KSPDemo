package com.rudolfhladik.kspdemo

import com.rudolfhladik.longpropertyannotation.LongCopyAnnotation

class Greeting {
    private val platform: Platform = getPlatform()

    @LongCopyAnnotation
    private val number: Int = 2

    fun greet(): String {

        return "Hello, ${platform.name} $number! ${Foo.generated}"
    }
}
