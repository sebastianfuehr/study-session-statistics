package de.berlin.vivepassion.io.database

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.entities.{Record, Semester, StudyDay}
import de.berlin.vivepassion.testspecs.VPStatSpec

class DBControllerTest extends VPStatSpec {


  "The database" should "contain one course after the course table was created and filled" in {
    dbTestController.createCourseTable()
    dbTestRepository.saveCourse("Introduction to Programming with Java")
    assert(dbTestRepository.getCourses().length == 1)
  }

  it should "test create study_form database table" in {
    dbTestController.createStudyFormTable()
    dbTestRepository.saveStudyForm("Calculate Problem Sets")
    assert(dbTestRepository.getStudyForms().length == 1)
  }

  it should "test create studyForm database table" in {
    dbTestController.createSemesterTable
    dbTestRepository.saveSemester(
      Semester(0, "SS19", LocalDate.parse("2019-04-04"), LocalDate.parse("2019-10-31")))
    assert(dbTestRepository.getSemesters.length == 1)
  }

  it should "test create study_day database table" in {
    dbTestController.createStudyDayTable
    dbTestRepository.saveStudyDay(StudyDay(0, LocalDate.parse("2019-10-10"), 5, ":)"))
    assert(dbTestRepository.getStudyDays.length == 1)
  }

  it should "test create record database table" in {
    dbTestController.createRecordTable
    dbTestRepository.saveRecord(Record(0, "Calculate Problem Sets",
      "Introduction to Programming with Java", LocalDateTime.parse("2019-10-10T15:00"),
      LocalDateTime.parse("2019-10-10T15:45"), 5, true, ":-)", "SS19"))
    assert(dbTestRepository.getRecords.length == 1)
  }

  it should "test clear all tables" in {
    dbTestController.clearAllTables()
    assert(dbTestRepository.getRecords.isEmpty
      && dbTestRepository.getStudyDays.isEmpty
      && dbTestRepository.getSemesters.isEmpty
      && dbTestRepository.getCourses.isEmpty
      && dbTestRepository.getStudyForms.isEmpty)
  }


  dbTestController.clearAllTables()

}
