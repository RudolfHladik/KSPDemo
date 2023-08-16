package com.rudolfhladik.propertyprocessor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream
import java.io.OutputStreamWriter

class PropertyVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSVisitorVoid() {

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        createNewSourceFile(property)
    }

    private fun createNewSourceFile(property: KSPropertyDeclaration) {
        logger.warn("PropertyVisitor: containing file ${property.containingFile}")
        logger.warn("PropertyVisitor: type ${property.type}")
        logger.warn("PropertyVisitor: location ${property.location}")
        logger.warn("PropertyVisitor: modifiers ${property.modifiers}")
        logger.warn("PropertyVisitor: parent ${property.parent}")

        property.containingFile?.let { file ->
            createNewFile(file)
        }
    }

    private fun createNewFile(containingFile: KSFile) {
        val dependencies = Dependencies(aggregating = false, containingFile)
        codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName = Deps.packageName,
            fileName = "Foo",
            extensionName = "kt"
        )
            .use { output ->
                checkOutputSourceSet()
                output.writeFooClass()
            }
    }

    private fun OutputStream.writeFooClass() {
        OutputStreamWriter(this).use { writer ->

            writer.write("package com.rudolfhladik.kspdemo\n\n")
            writer.write("class Foo {\n")
            writer.write("\n")
            writer.write("    companion object {\n")
            writer.write("        const val generated = \"generated\"\n")
            writer.write("    }\n")
            writer.write("}\n")
        }
    }

    private fun checkOutputSourceSet() {
        val outputSourceSet = codeGenerator.generatedFile
            .first()
            .toString()
            .sourceSetBelow("ksp")

        logger.warn("processing output source set '$outputSourceSet'")
    }

    private fun String.sourceSetBelow(startDirectoryName: String): String =
        substringAfter("/$startDirectoryName/").substringBefore("/kotlin/")
            .substringAfterLast('/')
}