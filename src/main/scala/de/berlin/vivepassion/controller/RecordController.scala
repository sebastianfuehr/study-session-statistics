package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.VPStats.dbRepository

/** Controller class for study sessions. */
object RecordController {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

  /**
   *
   * @param alone
   * @param form The form of learning.
   * @param course The course which was studied for.
   * @param startTimeString String which represents the start time of the study session.
   * @param comment A comment describing the study session.
   */
  def startNewStudySession(alone: Boolean, form: String, course: String, startTimeString: String,
                           comment: String) = {
    if (form != null && !dbRepository.getStudyForms.contains(form)) dbRepository.saveStudyForm(form)
    if (course != null && !dbRepository.getCourses.contains(course)) dbRepository.saveCourse(course)
  }

}
