package de.berlin.vivepassion.io.database

import java.time.LocalDateTime

import de.berlin.vivepassion.entities.{Course, Record, Semester, StudyForm}
import de.berlin.vivepassion.io.CSVFileLoader
import de.berlin.vivepassion.testspecs.VPStatSpec
import org.scalatest.BeforeAndAfterAll

class DBRepositoryTest extends VPStatSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = dbTestController.clearAllTables()


  "The DBRepository" should "save all records of a csv file in the database" in {
    CSVFileLoader.importCsvIntoDatabase(testProperties.getProperty("test_csv_table_path"), dbTestEntityController)
    // wie testen?
  }

  it should "retrieve all courses as a list" in {
    assert(dbTestRepository.getCourses.length === 2)
  }

  it should "retrieve all semesters as a list" in {
    assert(dbTestRepository.getSemesters.length === 1)
  }

  it should "retrieve all study forms as a list" in {
    assert(dbTestRepository.getStudyForms.length === 3)
  }

  ignore should "retrieve all study days as a list"

  it should "retrieve all records as a list" in {
    assert(dbTestRepository.getRecords.length === 6)
  }

  it should "retrieve the last record" in  {
    val record = Record(6, Some("doing homework"), Some("LinA"), LocalDateTime.parse("2018-01-06T20:00"),
      Some(LocalDateTime.parse("2018-01-06T21:30")), 10, true, None, "SS19")
    assert(dbTestRepository.getLastRecord === record)
  }



  "The SQLite database" should "retrieve all study sessions alone" in {
    val sqlStatement = "id, form, course, start_time, end_time, pause, alone, comment, semester_Name"
    val resultSet = dbTestRepository.queryTableFor("RECORD WHERE ALONE = 1", sqlStatement)
    val resultList = Record.resultSetToList(resultSet)
    assert(resultList.length === 5)
  }

  it should "retrieve all study sessions in a group" in {
    val sqlStatement = "id, form, course, start_time, end_time, pause, alone, comment, semesterName"
    val resultSet = dbTestRepository.queryTableFor("FROM RECORD WHERE ALONE = 0", sqlStatement)
    val resultList = Record.resultSetToList(resultSet)
    assert(resultList.length === 1)
  }

  ignore should "test retrieve all study sessions for a specific day"

  ignore should "test retrieve all study sessions of a specific month"

  ignore should "test retrieve all study sessions of a specific studyFormName"

  it should "retrieve all semesters" in {
    val resultSet = dbTestRepository.queryTableFor("SEMESTER", "*")
    val resultList = Semester.resultSetToList(resultSet)
    assert(resultList.length === 1)
  }

  it should "retrieve all courses" in {
    val resultSet = dbTestRepository.queryTableFor("course", "*")
    val resultList = Course.resultSetToList(resultSet)
    assert(resultList.length === 2)
  }

  it should "test retrieve all forms of study" in {
    val resultSet = dbTestRepository.queryTableFor("study_form", "*")
    val resultList = StudyForm.resultSetToList(resultSet)
    assert(resultList.length === 3)
  }

  it should "test retrieve all study sessions" in {
    val resultSet = dbTestRepository.queryTableFor("record", "*")
    val resultList = Record.resultSetToList(resultSet)
    assert(resultList.length === 6)
  }

  ignore should "test retrieve all study days"

  ignore should "test retrieve all study days of a specific studyFormName"

  ignore should "test retrieve a specific study day"


  override def afterAll(): Unit = dbTestController.clearAllTables()

}
