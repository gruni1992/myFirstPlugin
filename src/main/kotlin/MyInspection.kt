import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.PhpClassFieldsList
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class MyInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

        return object : PhpElementVisitor() {

            override fun visitPhpClassFieldsList(classFieldsList: PhpClassFieldsList?) {
                super.visitPhpClassFieldsList(classFieldsList)
                classFieldsList?.fields
                    ?.filter { it.docComment?.varTag != null }
                    ?.map {
                        InspectionManager.getInstance(holder.project)
                            .createProblemDescriptor(
                                it.docComment!!,
                                it,
                                "Add type to field declaration instead of applying it in @var tag.",
                                ProblemHighlightType.WARNING,
                                isOnTheFly
                            )
                    }?.forEach { holder.registerProblem(it); }
            }
        }
    }
}