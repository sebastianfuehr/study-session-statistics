package de.berlin.vivepassion.testspecs

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.io.database.{DBController, DBRepository}
import org.scalatest.FlatSpec

/** Test class which provides essential fields for all test classes. */
class VPStatSpec extends FlatSpec {

  /** Path of the 'vpstats_test.properties' file. */
  val testPropertiesPath = "./src/main/resources/vpstats_test.properties"
  val testProperties: Properties = new Properties()
  testProperties.load(new FileInputStream(testPropertiesPath))

  val dbTestController: DBController = new DBController(testProperties.getProperty("test_db_url"))
  val dbTestRepository: DBRepository = new DBRepository(dbTestController)

  dbTestController.createDatabase()

}
