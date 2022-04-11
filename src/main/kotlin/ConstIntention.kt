import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.ide.util.MemberChooser
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parents
import com.intellij.util.containers.toArray
import com.jetbrains.php.lang.actions.PhpNamedElementNode
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.PhpClassFieldsList
import com.jetbrains.php.lang.psi.elements.PhpModifier
import java.util.*

open class ConstIntention : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText() = "Create class const"

    override fun getFamilyName() = "ConstIntention"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        return when {
            element.parents(true).any { it is PhpClassFieldsList } -> true
            else -> false

        }
    }


    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val phpClass = element.parents(true)
            .filterIsInstance<PhpClass>()
            .first()
        val memberChooser = MemberChooser(allMembers(phpClass), false, true, project)
        memberChooser.show()
        memberChooser.selectedElements?.filterIsInstance<PhpNamedElementNode>()
            ?.map { it.psiElement }
            ?.filterIsInstance<Field>()
            ?.forEach { createConsts(it, project, element.containingFile) }
    }

    private fun createConsts(
        field: Field,
        project: Project,
        file: PsiFile
    ) {
        val const = PhpPsiElementFactory.createClassConstant(
            project,
            PhpModifier.PUBLIC_IMPLEMENTED_DYNAMIC,
            field.name.camelToSnakeCase().uppercase(Locale.getDefault()),
            "\"${field.name}\""
        )
        val newline = PhpPsiElementFactory.createNewLine(project)
        file.add(newline)
        file.add(const)
    }

    private fun allMembers(phpClass: PhpClass): Array<ClassMember> = phpClass.fields
        .filter { !it.isConstant }
        .map { PhpNamedElementNode(it) }
        .toArray(ClassMember.EMPTY_ARRAY)
}