package org.jetbrains.plugins.hocon
package settings

import java.io.File

import com.github.ghik.silencer.silent
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components._
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

import scala.beans.BeanProperty

@State(
  name = "HoconProjectSettings",
  storages = Array(
    new Storage(StoragePathMacros.WORKSPACE_FILE),
    new Storage("hocon_settings.xml")
  )
)
@silent("deprecated")
class HoconProjectSettings extends PersistentStateComponent[HoconProjectSettings] with ExportableComponent {
  def getState: HoconProjectSettings = this

  def loadState(state: HoconProjectSettings): Unit =
    XmlSerializerUtil.copyBean(state, this)

  def getPresentableName = "HOCON Project Settings"

  def getExportFiles: Array[File] =
    Array(PathManager.getOptionsFile("hocon_project_settings"))

  @BeanProperty var classReferencesOnUnquotedStrings = true
  @BeanProperty var classReferencesOnQuotedStrings = true
  @BeanProperty var propertyReferencesOnStrings = true
  @BeanProperty var searchInGotoSymbol = false
}

object HoconProjectSettings {
  def getInstance(project: Project): HoconProjectSettings =
    ServiceManager.getService(project, classOf[HoconProjectSettings])
}
