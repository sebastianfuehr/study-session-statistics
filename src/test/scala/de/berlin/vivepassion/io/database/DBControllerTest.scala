package de.berlin.vivepassion.io.database

import java.io.FileInputStream
import java.time.{LocalDate, LocalDateTime}
import java.util.Properties

import de.berlin.vivepassion.entities.{Record, Semester, StudyDay}
import org.scalatest.{BeforeAndAfter, FunSuite}

class DBControllerTest extends FunSuite with BeforeAndAfter {

  /** Path of the 'vpstats_test.properties' file. */
  val testPropertiesPath = "./src/main/resources/vpstats_test.properties"
  val testProperties: Properties = new Properties()
  testProperties.load(new FileInputStream(testPropertiesPath))

  val dbTestController: DBController = new DBController(testProperties.getProperty("test_db_url"))
  val dbTestRepository: DBRepository = new DBRepository(dbTestController)

  before {
    dbTestController.createDatabase
    dbTestController.clearAllTables
  }

  test ("test create course database table") {
    dbTestController.createCourseTable
    dbTestRepository.saveCourse("Introduction to Programming with Java")
    assert(dbTestRepository.getCourses.length == 1)
  }

  test ("test create study_form database table") {
    dbTestController.createStudyFormTable
    dbTestRepository.saveStudyForm("Calculate Problem Sets")
    assert(dbTestRepository.getStudyForms.length == 1)
  }

  test ("test create studyForm database table") {
    dbTestController.createSemesterTable
    dbTestRepository.saveSemester(
      Semester(0, "SS19", LocalDate.parse("2019-04-04"), LocalDate.parse("2019-10-31")))
    assert(dbTestRepository.getSemesters.length == 1)
  }

  test ("test create study_day database table") {
    dbTestController.createStudyDayTable
    dbTestRepository.saveStudyDay(StudyDay(0, LocalDate.parse("2019-10-10"), 5, ":)"))
    assert(dbTestRepository.getStudyDays.length == 1)
  }

  test ("test create record database table") {
    dbTestController.createRecordTable
    dbTestRepository.saveRecord(Record(0, "Calculate Problem Sets",
      "Introduction to Programming with Java", LocalDateTime.parse("2019-10-10T15:00"),
      LocalDateTime.parse("2019-10-10T15:45"), 5, true, ":-)", "SS19"))
    assert(dbTestRepository.getRecords.length == 1)
  }

  test ("test clear all tables") {
    dbTestController.clearAllTables
    assert(dbTestRepository.getRecords.isEmpty
      && dbTestRepository.getStudyDays.isEmpty
      && dbTestRepository.getSemesters.isEmpty
      && dbTestRepository.getCourses.isEmpty
      && dbTestRepository.getStudyForms.isEmpty)
  }

  after {
    dbTestController.clearAllTables
  }

}
