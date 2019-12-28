package de.berlin.vivepassion.testspecs

import de.berlin.vivepassion.io.CSVFileLoader
import de.berlin.vivepassion.testspecs.VPStatTestConfig._
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

/**
 * Class which provides a local database with entries in all tables for
 * testing classes which want to manipulate the database.
 *
 * @note This class depends on the functionality of methods of other classes.
 *
 * @see CSVFileLoader#importCsvIntoDatabase
 * @see RecordTests
 * @see DbRepositoryRecordTest
 *
 * @author Sebastian Führ
 * @version 0.1
 */
abstract class VPStatTestDB extends FlatSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    dbTestController.createDatabase()
    dbTestController.clearAllTables()
    CSVFileLoader.importCsvIntoDatabase(testCsvFilePath, dbTestEntityController)
  }

  override def afterAll(): Unit = {
    dbTestController.deleteDatabase()
  }

}
