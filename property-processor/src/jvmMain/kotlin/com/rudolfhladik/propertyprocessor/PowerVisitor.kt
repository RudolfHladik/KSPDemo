package com.rudolfhladik.propertyprocessor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

class PowerVisitor(
    private val logger: KSPLogger
) : KSVisitorVoid() {
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val modifiers = classDeclaration.modifiers
        modifiers.forEach {
            logger.warn("PowerVisitor modifiers: $it")
        }

        val constructorParams = classDeclaration.primaryConstructor?.parameters?.map { "${it}: ${it.type}" }
        constructorParams?.forEach {
            logger.warn("PowerVisitor constructor param: $it")
        }

        val parents = classDeclaration.superTypes
        parents.forEach {
            logger.warn("PowerVisitor parent: $it ")
        }

        val properties = classDeclaration.getAllFunctions()
        properties.forEach {
            val funParams = it.parameters.map { "${it}: ${it.type}, " }
            logger.warn("PowerVisitor functions: ${it.modifiers} fun $it($funParams): ${it.returnType} ")
        }
    }
}
