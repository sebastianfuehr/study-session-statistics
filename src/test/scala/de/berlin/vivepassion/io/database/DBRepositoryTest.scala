package de.berlin.vivepassion.io.database

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.entities.{Record, StudyDay}
import de.berlin.vivepassion.testspecs.VPStatSpec
import org.sqlite.SQLiteException

class DBRepositoryTest extends VPStatSpec {

  dbTestController.clearAllTables()


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
        LocalDateTime.parse("2019-10-10T15:45"), 5, true, ":-)", "SS19"))
    }
    assert(thrown.isInstanceOf[SQLiteException])
  }


  dbTestController.clearAllTables()


  "The database" should "contain two courses if a csv file was imported" in {
    dbTestRepository.importCsvIntoDatabase(testProperties.getProperty("test_csv_table_path"), "WS18/19")
    assert(dbTestRepository.getCourses.length == 2)
  }

  "The DBRepository" should "retrieve all study sessions alone" in {
    val resultSet = dbTestRepository.queryDatabaseFor("SELECT * FROM record WHERE alone = 1")
    val resultList = Record.fromResultSet(resultSet)
    assert(resultList.length == 5)
  }

  ignore should "test retrieve all study sessions in a group"

  ignore should "test retrieve all study sessions for a specific day"

  ignore should "test retrieve all study sessions of a specific month"

  ignore should "test retrieve all study sessions of a specific studyForm"

  ignore should "test retrieve all semesters"

  ignore should "test retrieve all courses"

  ignore should "test retrieve all forms of study"

  ignore should "test retrieve all study sessions"

  ignore should "test retrieve all study days"

  ignore should "test retrieve all study days of a specific studyForm"

  ignore should "test retrieve a specific study day"


  dbTestController.clearAllTables()

}
