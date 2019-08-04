package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.VPStats.dbRepository

object StudyFormController {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

  /**
   * Saves the study form in the database.
   * @param studyForm The name of the studyForm to be saved.
   */
  def createStudyFormIfNotExists(studyForm: String) = {
    if (studyForm != null && !dbRepository.getStudyForms.contains(studyForm)) {
      dbRepository.saveStudyForm(studyForm)
    }
  }

}
