package com.rudolfhladik.propertyprocessor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import java.io.OutputStreamWriter
import kotlin.reflect.KClass

class PropertyProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {

    var wasInvoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {

//        val annotatedProperty: KSPropertyDeclaration = resolver.findAnnotations(LongCopyAnnotation::class).first()

        if (wasInvoked) return emptyList()
        logger.warn("annotated properties")

        createNewSourceFile()

        wasInvoked = true

        return emptyList()
    }

    private fun createNewSourceFile() {
        codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = "com.rudolfhladik.kspdemo",
            "Foo"
        )
            .use { output ->
            OutputStreamWriter(output).use { writer ->
                writer.write("package com.rudolfhladik.kspdemo\n\n")
                writer.write("class Foo {\n")

                writer.write("}\n")
            }
        }
    }

    private fun Resolver.findAnnotations(kClass: KClass<*>): Sequence<KSPropertyDeclaration> =
        this.getSymbolsWithAnnotation(kClass.qualifiedName.toString())
            .filterIsInstance<KSPropertyDeclaration>()
//            .filter { it.type.resolve().declaration as? KSClassDeclaration == this.getClassDeclarationByName("Int") }
//            .filter { it.typeParameters.first().name == Int::class }
}

class PropertyProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return PropertyProcessor(environment.codeGenerator, environment.logger)
    }
}
