package com.demonwav.mcdev.platform.fabric.folding

import com.demonwav.mcdev.util.constantValue
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope

class FabricFoldingBuilder : CustomFoldingBuilder() {

    override fun isDumbAware(): Boolean = false

    override fun isRegionCollapsedByDefault(node: ASTNode): Boolean = true

    override fun buildLanguageFoldRegions(descriptors: MutableList<FoldingDescriptor>, root: PsiElement, document: Document, quick: Boolean) {
        if (root is PsiJavaFile) {
            root.accept(Visitor(descriptors))
        }
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String {
        val constantValue = node.psi.constantValue

        if (constantValue != null) {
            loadConstants(node.psi.project)[constantValue]?.let { return it }
        }

        return node.text
    }

    private class Visitor(private val descriptors: MutableList<FoldingDescriptor>) :
            JavaRecursiveElementWalkingVisitor() {
        override fun visitLiteralExpression(expression: PsiLiteralExpression?) {
            super.visitLiteralExpression(expression)

            if (expression?.constantValue is Int) {
                val project = expression.project

                JavaPsiFacade.getInstance(project).findClass("", GlobalSearchScope.allScope(project))
                descriptors.add(FoldingDescriptor(expression, expression.textRange))
            }
        }
    }
}

private fun loadConstants(project: Project): Map<Int, String> {
    val map = mutableMapOf<Int, String>()
    val java = JavaPsiFacade.getInstance(project)
    val scope = GlobalSearchScope.allScope(project)

    for (className in listOf(
            "org.lwjgl.opengl.GL11",
            "org.lwjgl.opengl.GL12",
            "org.lwjgl.opengl.GL13",
            "org.lwjgl.opengl.GL14",
            "org.lwjgl.opengl.GL15",
            "org.lwjgl.opengl.GL20",
            "org.lwjgl.opengl.GL21",
            "org.lwjgl.opengl.GL30",
            "org.lwjgl.opengl.GL31",
            "org.lwjgl.opengl.GL32",
            "org.lwjgl.opengl.GL33",
            "org.lwjgl.opengl.GL40",
            "org.lwjgl.opengl.GL41",
            "org.lwjgl.opengl.GL42",
            "org.lwjgl.opengl.GL43",
            "org.lwjgl.opengl.GL44",
            "org.lwjgl.opengl.GL45",
            "org.lwjgl.opengl.GL46")) {
        val c = java.findClass(className, scope)
        val f = c?.allFields
        f?.forEach { field ->
            val constantValue = field.computeConstantValue()

            if (constantValue is Int) {
                map.putIfAbsent(constantValue, className.split(".").last() + "." + field.name)
            }
        }
    }

    return map
}
