package de.berlin.vivepassion.io.database

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.entities.{Record, Semester, StudyDay}
import org.scalatest.{BeforeAndAfter, FunSuite}

class DBControllerTest extends FunSuite with BeforeAndAfter {

  val dbController: DBController = new DBController(VPSConfiguration.properties.getProperty("test_db_url"))
  val dbRepository: DBRepository = new DBRepository(dbController)

  before {
    dbController.createDatabase
    dbController.clearAllTables
  }

  test ("test create course database table") {
    dbController.createCourseTable
    dbRepository.saveCourse("Introduction to Programming with Java")
    assert(dbRepository.getCourses.length == 1)
  }

  test ("test create study_form database table") {
    dbController.createStudyFormTable
    dbRepository.saveStudyForm("Calculate Problem Sets")
    assert(dbRepository.getStudyForms.length == 1)
  }

  test ("test create studyForm database table") {
    dbController.createSemesterTable
    dbRepository.saveSemester(
      Semester(0, "SS19", LocalDate.parse("2019-04-04"), LocalDate.parse("2019-10-31")))
    assert(dbRepository.getSemesters.length == 1)
  }

  test ("test create study_day database table") {
    dbController.createStudyDayTable
    dbRepository.saveStudyDay(StudyDay(0, LocalDate.parse("2019-10-10"), 5, ":)"))
    assert(dbRepository.getStudyDays.length == 1)
  }

  test ("test create record database table") {
    dbController.createRecordTable
    dbRepository.saveRecord(Record(0, "Calculate Problem Sets",
      "Introduction to Programming with Java", LocalDateTime.parse("2019-10-10T15:00"),
      LocalDateTime.parse("2019-10-10T15:45"), 5, true, ":-)", "SS19"))
    assert(dbRepository.getRecords.length == 1)
  }

  test ("test clear all tables") {
    dbController.clearAllTables
    assert(dbRepository.getRecords.isEmpty
      && dbRepository.getStudyDays.isEmpty
      && dbRepository.getSemesters.isEmpty
      && dbRepository.getCourses.isEmpty
      && dbRepository.getStudyForms.isEmpty)
  }

  after {
    dbController.clearAllTables
  }

}
