package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.VPStats.dbRepository

import scala.io.StdIn

object CourseController {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

  /**
   * If the name of the course form doesn't exist yet the method
   * {@link #createCourse() createCourse} is called.
   * @param course The name of the course to be saved.
   */
  def createCourseIfNotExists(course: String) = {
    if (course != null && !dbRepository.getCourses.contains(course)) {
      println(s"The course $course was not found. A new one has to be created.")
      createCourse()
    }
  }

  /** Prompts the user for a name for the new course and saves it into the database. */
  def createCourse() = {
    val name = StdIn.readLine("What is the name of the new course?")
    dbRepository.saveCourse(name)
  }

}
