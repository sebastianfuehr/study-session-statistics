package de.berlin.vivepassion.io.database

import java.io.FileInputStream
import java.time.{LocalDate, LocalDateTime}
import java.util.Properties

import de.berlin.vivepassion.entities.{Record, StudyDay}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.sqlite.SQLiteException

class DBRepositoryTest extends FunSuite with BeforeAndAfter {

  /** Path of the 'vpstats_test.properties' file. */
  val testPropertiesPath = "./src/main/resources/vpstats_test.properties"
  val testProperties: Properties = new Properties()
  testProperties.load(new FileInputStream(testPropertiesPath))

  val dbTestController: DBController = new DBController(testProperties.getProperty("test_db_url"))
  val dbTestRepository: DBRepository = new DBRepository(dbTestController)

  dbTestController.createDatabase

  test ("test try inserting NULL attribute in study_day table") {
    val thrown = intercept[Exception] {
      dbTestRepository.saveStudyDay(StudyDay(0, LocalDate.parse("2019-10-10"), 0, null))
    }
    assert(thrown.isInstanceOf[SQLiteException])
  }

  test ("test inserting NULL attribute in record table") {
    val thrown = intercept[Exception] {
      dbTestRepository.saveRecord(Record(0, null,
        "Introduction to Programming with Java", LocalDateTime.parse("2019-10-10T15:00"),
        LocalDateTime.parse("2019-10-10T15:45"), 5, true, ":-)", "SS19"))
    }
    assert(thrown.isInstanceOf[SQLiteException])
  }

  test ("test retrieve all study sessions alone") (pending)

  test ("test retrieve all study sessions in a group") (pending)

  test ("test retrieve all study sessions for a specific day") (pending)

  test ("test retrieve all study sessions of a specific month") (pending)

  test ("test retrieve all study sessions of a specific studyForm") (pending)

  test ("test retrieve all semesters") (pending)

  test ("test retrieve all courses") (pending)

  test ("test retrieve all forms of study") (pending)

  test ("test retrieve all study sessions") (pending)

  test ("test retrieve all study days") (pending)

  test ("test retrieve all study days of a specific studyForm") (pending)

  test ("test retrieve a specific study day") (pending)

  dbTestController.clearAllTables

  test ("test import csv file into database") {
    dbTestRepository.importCsvIntoDatabase(testProperties.getProperty("test_csv_table_path"), "WS18/19")
    assert(dbTestRepository.getCourses.length == 2)
  }

  after {
    dbTestController.clearAllTables
  }

}
