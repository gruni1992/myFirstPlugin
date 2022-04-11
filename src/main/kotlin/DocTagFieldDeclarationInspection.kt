import com.intellij.codeInspection.*
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.codeStyle.CodeStyleManager
import com.jetbrains.php.codeInsight.PhpImportOptimizer
import com.jetbrains.php.lang.PhpCodeUtil
import com.jetbrains.php.lang.actions.generation.PhpGenerateGettersAction
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpClassFieldsList
import com.jetbrains.php.lang.psi.elements.PhpModifier
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class DocTagFieldDeclarationInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

        return object : PhpElementVisitor() {

            override fun visitPhpClassFieldsList(classFieldsList: PhpClassFieldsList?) {
                super.visitPhpClassFieldsList(classFieldsList)
                classFieldsList?.fields
                    ?.filter { it.typeDeclaration == null }
                    ?.filter { it.docComment?.varTag != null }
                    ?.map {
                        InspectionManager.getInstance(holder.project)
                            .createProblemDescriptor(
                                it.docComment!!,
                                it,
                                "Add type to field declaration instead of applying it in @var tag",
                                ProblemHighlightType.WARNING,
                                isOnTheFly,
                                QuickFix()
                            )
                    }?.forEach { holder.registerProblem(it) }
            }
        }
    }

    class QuickFix : LocalQuickFix {
        override fun getFamilyName(): String = name

        override fun getName(): String = "Refactor type declaration"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val type = (descriptor.startElement as PhpDocComment).varTag?.type
            val field = (descriptor.endElement as Field)
            val newElement = PhpPsiElementFactory.createClassField(
                project,
                field.modifier,
                field.name,
                field.defaultValuePresentation,
                type!!.toStringResolved()
            )
            descriptor.psiElement.addAfter(newElement, field.parent)
            descriptor.psiElement.deleteChildRange(descriptor.startElement, field.parent)
            CodeStyleManager.getInstance(project)
        }

    }
}

