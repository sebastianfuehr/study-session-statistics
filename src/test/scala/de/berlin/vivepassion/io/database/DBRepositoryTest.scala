package de.berlin.vivepassion.io.database

import java.time.LocalDateTime

import de.berlin.vivepassion.entities.Record
import de.berlin.vivepassion.io.CSVFileLoader
import de.berlin.vivepassion.testspecs.VPStatTestConfig._
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

/**
 * A test class for testing the functionality of the class VPStatsDBRepository.
 *
 * @note The class depends on functioning methods of other classes.
 *
 * @see VPStatsDBController#clearAllTables
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class DBRepositoryTest extends FlatSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = dbTestController.clearAllTables()



  "The DBRepository" should "save all records of a csv file in the database" in {
    CSVFileLoader.importCsvIntoDatabase(testCsvFilePath, dbTestEntityController)
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

  ignore should "retrieve all study days as a list" in {

  }

  it should "retrieve all records as a list" in {
    assert(dbTestRepository.getRecords.length === 6)
  }

  it should "retrieve the last record" in  {
    val record = Record(6, Some("doing homework"), Some("LinA"), LocalDateTime.parse("2018-01-06T20:00"),
      Some(LocalDateTime.parse("2018-01-06T21:30")), 10, alone = true, None, "SS19")
    assert(dbTestRepository.getLastRecord === record)
  }



  override def afterAll(): Unit = dbTestController.clearAllTables()

}
