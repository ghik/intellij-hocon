package org.jetbrains.plugins.hocon

import com.intellij.openapi.actionSystem._
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.{FileEditorManager, OpenFileDescriptor}
import com.intellij.psi.PsiFile
import org.junit.Assert.assertNotNull

/**
 * @author ghik
 */
abstract class HoconActionTest protected(protected val actionId: String, subPath: String)
  extends HoconFileSetTestCase(s"actions/$subPath") {

  // Code based on AbstractEnterActionTestBase

  import HoconActionTest._
  import HoconFileSetTestCase._

  override protected def transform(data: Seq[String]): String = {
    val (fileText, offset) = extractCaret(data.head)
    val psiFile = createPseudoPhysicalHoconFile(fileText)

    val editorManager = FileEditorManager.getInstance(myProject)
    val editor: Editor = editorManager.openTextEditor(
      new OpenFileDescriptor(myProject, psiFile.getVirtualFile, 0), false)
    assertNotNull(editor)
    editor.getCaretModel.moveToOffset(offset)

    try {
      executeAction(new MockDataContext(psiFile, editor), editor)
    } finally {
      editorManager.closeFile(psiFile.getVirtualFile)
    }
  }

  protected def executeAction(dataContext: DataContext, editor: Editor): String
}

object HoconActionTest {

  private class MockDataContext(file: PsiFile, editor: Editor) extends DataContext with DataProvider {
    def getData(dataId: String): AnyRef =
      if (LangDataKeys.LANGUAGE is dataId) file.getLanguage
      else if (CommonDataKeys.PROJECT is dataId) file.getProject
      else if (CommonDataKeys.EDITOR is dataId) editor
      else null
  }

}
