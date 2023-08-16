package com.rudolfhladik.kspdemo

import com.rudolfhladik.longpropertyannotation.Power

@Power
internal data class PowerClass(val name: String, val strength: Int) : PowerAbstract(), PowerInterface {

    override val power: Int
        get() = calculateSuperPower()

    fun getMySuperPower() = getSuperPower(name)

    private fun calculateSuperPower(): Int = strength * 10
}

interface PowerInterface {
    val power: Int
}

abstract class PowerAbstract {
    fun getSuperPower(name: String): String = if (name == "Hulk") {
        "BadaBoom!!"
    } else {
        "Thunder!"
    }
}
