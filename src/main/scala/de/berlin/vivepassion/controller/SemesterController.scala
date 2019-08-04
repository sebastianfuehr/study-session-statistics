package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.VPStats.dbRepository
import de.berlin.vivepassion.entities.Semester

import scala.io.StdIn

object SemesterController {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

  /**
   * If the name of the semester doesn't exist yet the method
   * {@link #createSemester() createSemester} is called.
   * @param semester The name of the semester to be saved.
   */
  def createSemesterIfNotExists(semester: String): Unit = {
    if (semester != null && !dbRepository.getSemesters.contains(semester)) {
      println(s"The semester '$semester' was not found. A new one has to be created.")
      createSemester(semester)
    }
  }

  /**
   * Prompts the user for a semester name, start and end date to create a new
   * entity and saves it in the database. The id of the semester instance is -1
   * because it is ignored by the
   * {@link DBRepository#saveSemester(Semester) saveSemester}
   * method.
   * @param name The name of the semester to be saved.
   */
  def createSemester(name: String) = {
    val startDateStr = StdIn.readLine("Which date is the first day of the semester?")
    val endDateStr = StdIn.readLine("Which is the last day?")

    val dateFormatStr: String = properties.getProperty("default_date_format")
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatStr)
    val startDate: LocalDate = LocalDate.parse(startDateStr, dateFormatter)
    val endDate: LocalDate = LocalDate.parse(endDateStr, dateFormatter)

    dbRepository.saveSemester(Semester(-1, name, startDate, endDate))
  }

}