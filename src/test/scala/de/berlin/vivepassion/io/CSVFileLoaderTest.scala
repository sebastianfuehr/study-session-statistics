package de.berlin.vivepassion.io

import java.io.FileInputStream
import java.util.Properties

import org.scalatest.{BeforeAndAfter, FunSuite}

class CSVFileLoaderTest extends FunSuite with BeforeAndAfter {

  /** Path of the 'vpstats_test.properties' file. */
  val testPropertiesPath = "./src/main/resources/vpstats_test.properties"
  val testProperties: Properties = new Properties()
  testProperties.load(new FileInputStream(testPropertiesPath))

  test ("test getListOfCSVFile") {
    val list = CSVFileLoader.getListOfCSVFile(testProperties.getProperty("test_csv_table_path"))
    assert(list.length == 4)
  }

}
