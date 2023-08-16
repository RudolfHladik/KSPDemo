package com.rudolfhladik.propertyprocessor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.rudolfhladik.longpropertyannotation.LongCopyAnnotation
import com.rudolfhladik.longpropertyannotation.Power
import kotlin.reflect.KClass

class PropertyProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols: Sequence<KSPropertyDeclaration> = resolver
            .findAnnotationsForProperties(LongCopyAnnotation::class)

        val powers = resolver
            .findAnnotationsForClass(Power::class)

        powers.forEach {
            it.accept(PowerVisitor(logger), Unit)
        }

        val hasNext = symbols.iterator().hasNext()
        logger.warn("has next $hasNext")

        // stop re-processing quick
        if (hasNext.not()) return emptyList()

        symbols.forEach {
            it.accept(PropertyVisitor(codeGenerator, logger), Unit)
        }

        return emptyList()
    }

    private fun Resolver.findAnnotationsForProperties(kClass: KClass<*>): Sequence<KSPropertyDeclaration> =
        this.getSymbolsWithAnnotation(kClass.qualifiedName.toString())
            .filterIsInstance<KSPropertyDeclaration>()

    private fun Resolver.findAnnotationsForClass(kClass: KClass<*>): Sequence<KSClassDeclaration> =
        this.getSymbolsWithAnnotation(kClass.qualifiedName.toString())
            .filterIsInstance<KSClassDeclaration>()
}
