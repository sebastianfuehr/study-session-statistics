package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.io.database.DBRepository

class CourseController(dbRepository: DBRepository) {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

  /**
   * Saves the course in the database.
   * @param course The name of the course to be saved.
   */
  def createCourseIfNotExists(course: String) = {
    if (course != null && !dbRepository.getCourses.contains(course)) {
      dbRepository.saveCourse(course)
    }
  }

}
