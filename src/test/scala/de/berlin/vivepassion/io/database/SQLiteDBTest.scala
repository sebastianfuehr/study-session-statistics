package de.berlin.vivepassion.io.database

import de.berlin.vivepassion.entities.{Course, Record, Semester, StudyForm}
import de.berlin.vivepassion.io.CSVFileLoader
import de.berlin.vivepassion.testspecs.VPStatTestConfig._
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

/**
 * A test class only to verify that the SQLite database works at it should do.
 *
 * @note The class depends on functioning methods of other classes.
 *
 * @see CSVFileLoader#importCsvIntoDatabase
 * @see VPStatsDBController#clearAllTables
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class SQLiteDBTest extends FlatSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    dbTestController.createDatabase()
    dbTestController.clearAllTables()
    CSVFileLoader.importCsvIntoDatabase(testCsvFilePath, dbTestEntityController)
  }



  "The SQLite database" should "retrieve all study sessions alone" in {
    val sqlStatement = "id, form, course, start_time, end_time, pause, alone, comment, semester"
    val resultSet = dbTestRepository.queryTableFor("STUDY_SESSION WHERE ALONE = 1", sqlStatement)
    val resultList = Record.resultSetToList(resultSet)
    assert(resultList.length === 5)
  }

  it should "retrieve all study sessions in a group" in {
    val sqlStatement = "id, form, course, start_time, end_time, pause, alone, comment, semester"
    val resultSet = dbTestRepository.queryTableFor("STUDY_SESSION WHERE ALONE = 0", sqlStatement)
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
    val resultSet = dbTestRepository.queryTableFor("STUDY_SESSION", "*")
    val resultList = Record.resultSetToList(resultSet)
    assert(resultList.length === 6)
  }

  ignore should "test retrieve all study days"

  ignore should "test retrieve all study days of a specific studyFormName"

  ignore should "test retrieve a specific study day"



  override def afterAll(): Unit = dbTestController.clearAllTables()

}
