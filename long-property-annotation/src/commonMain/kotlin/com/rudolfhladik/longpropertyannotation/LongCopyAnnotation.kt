package com.rudolfhladik.longpropertyannotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class LongCopyAnnotation(
    val name: String = ""
)
