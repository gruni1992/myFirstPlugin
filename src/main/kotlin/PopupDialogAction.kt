import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages


class PopupDialogAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        // Using the event, create and show a dialog
        val currentProject: Project? = event.project
        val dlgMsg = StringBuffer(event.presentation.text + " Selected!")
        val psiFile = event.getData(LangDataKeys.PSI_FILE)!!
        dlgMsg.append(String.format("\nSelected Psi Element: %s", psiFile))
        val editor: Editor = event.getData(CommonDataKeys.EDITOR)!!
        val offset = editor.caretModel.offset
        val infoBuilder = StringBuilder()
        val element = psiFile.findElementAt(offset)
        infoBuilder.append("Element at caret: ").append(element).append("\n")
        Messages.showMessageDialog(currentProject, infoBuilder.toString(), "dlgTitle", Messages.getInformationIcon())
    }


}