package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.VPStats.dbRepository

import scala.io.StdIn

object StudyFormController {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

  /**
   * If the name of the study form doesn't exist yet the method
   * {@link #createStudyForm() createStudyForm} is called.
   * @param studyForm The name of the studyForm to be saved.
   */
  def createStudyFormIfNotExists(studyForm: String) = {
    if (studyForm != null && !dbRepository.getStudyForms.contains(studyForm)) {
      println(s"The studyForm $studyForm was not found. A new one has to be created.")
      createStudyForm()
    }
  }

  /** Prompts the user for a name for the new study form and saves it into the database. */
  def createStudyForm() = {
    val name = StdIn.readLine("What is the name of the new form of studying?")
    dbRepository.saveStudyForm(name)
  }

}
