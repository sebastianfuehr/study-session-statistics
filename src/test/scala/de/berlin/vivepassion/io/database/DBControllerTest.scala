package de.berlin.vivepassion.io.database

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.entities.{Record, StudyDay}
import org.scalatest.{BeforeAndAfter, FunSuite}

class DBControllerTest extends FunSuite with BeforeAndAfter {

  val dbController: DBController = new DBController(VPSConfiguration.properties.getProperty("test_db_url"))
  val dbRepository: DBRepository = new DBRepository(dbController)

  before {
    dbController.clearAllTables
  }

  test ("test create course database table") {
    dbController.createCourseTable
    dbRepository.saveCourse("Einführung in die Wirtschaftsinformatik")
    assert(dbRepository.getCourses.length == 1)
  }

  test ("test create study_form database table") {
    dbController.createStudyFormTable
    dbRepository.saveStudyForm("Übungsaufgaben rechnen")
    assert(dbRepository.getStudyForms.length == 1)
  }

  test ("test create semester database table") {
    dbController.createSemesterTable
    dbRepository.saveSemester("SS19")
    assert(dbRepository.getSemesters.length == 1)
  }

  test ("test create study_day database table") {
    dbController.createStudyDayTable
    dbRepository.saveStudyDay(StudyDay(0, LocalDate.parse("2019-10-10"), 5, ":)"))
    assert(dbRepository.getStudyDays.length == 1)
  }

  test ("test create record database table") {
    dbController.createRecordTable
    dbRepository.saveRecord(Record("Übungsaufgaben rechnen",
      "Einführung in die Wirtschaftsinformatik", LocalDateTime.parse("2019-10-10T15:00"),
      LocalDateTime.parse("2019-10-10T15:45"), 5, true, ":-)", 0))
    assert(dbRepository.getRecords.length == 1)
  }

  after {
    dbController.clearAllTables
  }

}
