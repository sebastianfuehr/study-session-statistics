package de.berlin.vivepassion.io.database

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.entities.{Course, Record, Semester, StudyDay, StudyForm}
import de.berlin.vivepassion.testspecs.VPStatSpec
import org.scalatest.BeforeAndAfterAll
import org.sqlite.SQLiteException

class DBRepositoryTest extends VPStatSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = dbTestController.clearAllTables()


  "The DBRepository" should "save all records of a csv file in the database" in {
    dbTestRepository.importCsvIntoDatabase(testProperties.getProperty("test_csv_table_path"), "WS18/19")
    // wie testen?
  }

  it should "retrieve all courses as a list" in {
    assert(dbTestRepository.getCourses().length === 2)
  }

  it should "retrieve all semesters as a list" in {
    assert(dbTestRepository.getSemesters().length === 1)
  }

  it should "retrieve all study forms as a list" in {
    assert(dbTestRepository.getStudyForms().length === 3)
  }

  ignore should "retrieve all study days as a list"

  it should "retrieve all records as a list" in {
    assert(dbTestRepository.getRecords().length === 6)
  }



  "The sql lite database" should "throw SQLiteException if a NULL attribute is inserted into the study_day table" in {
    val thrown = intercept[Exception] {
      dbTestRepository.saveStudyDay(StudyDay(0, LocalDate.parse("2019-10-10"), 0, null))
    }
    assert(thrown.isInstanceOf[SQLiteException])
  }

  it should "throw SQLiteException if a NULL attribute is inserted into the record table" in {
    val thrown = intercept[Exception] {
      dbTestRepository.saveRecord(Record(0, null,
        "Introduction to Programming with Java", LocalDateTime.parse("2019-10-10T15:00"),
        Some(LocalDateTime.parse("2019-10-10T15:45")), 5, true, ":-)", "SS19"))
    }
    assert(thrown.isInstanceOf[SQLiteException])
  }

  it should "retrieve all study sessions alone" in {
    val resultSet = dbTestRepository.queryDatabaseFor("SELECT * FROM record WHERE alone = 1")
    val resultList = Record.fromResultSet(resultSet)
    assert(resultList.length === 5)
  }

  it should "retrieve all study sessions in a group" in {
    val resultSet = dbTestRepository.queryDatabaseFor("SELECT * FROM record WHERE alone = 0")
    val resultList = Record.fromResultSet(resultSet)
    assert(resultList.length === 1)
  }

  ignore should "test retrieve all study sessions for a specific day"

  ignore should "test retrieve all study sessions of a specific month"

  ignore should "test retrieve all study sessions of a specific studyForm"

  it should "retrieve all semesters" in {
    val resultSet = dbTestRepository.queryDatabaseFor("SELECT * FROM semester")
    val resultList = Semester.fromResultSet(resultSet)
    assert(resultList.length === 1)
  }

  it should "retrieve all courses" in {
    val resultSet = dbTestRepository.queryDatabaseFor("SELECT * FROM course")
    val resultList = Course.fromResultSet(resultSet)
    assert(resultList.length === 2)
  }

  it should "test retrieve all forms of study" in {
    val resultSet = dbTestRepository.queryDatabaseFor("SELECT * FROM study_form")
    val resultList = StudyForm.fromResultSet(resultSet)
    assert(resultList.length === 3)
  }

  it should "test retrieve all study sessions" in {
    val resultSet = dbTestRepository.queryDatabaseFor("SELECT * FROM record")
    val resultList = Record.fromResultSet(resultSet)
    assert(resultList.length === 6)
  }

  ignore should "test retrieve all study days"

  ignore should "test retrieve all study days of a specific studyForm"

  ignore should "test retrieve a specific study day"


  override def afterAll(): Unit = dbTestController.clearAllTables()

}
