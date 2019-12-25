package de.berlin.vivepassion.io

import de.berlin.vivepassion.testspecs.VPStatTestConfig._
import org.scalatest.FlatSpec

/**
 * A test class for testing the functionality of the class CSVFileLoader
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class CSVFileLoaderTest extends FlatSpec {


  "CSVFileLoader" should "load a csv file and return the persisted records as a list" in {
    val list = CSVFileLoader.getListOfCSVFile(testCsvFilePath)
    assert(list.length == 6)
  }


}
