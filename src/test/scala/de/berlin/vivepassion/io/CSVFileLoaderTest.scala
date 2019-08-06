package de.berlin.vivepassion.io

import de.berlin.vivepassion.testspecs.VPStatSpec

class CSVFileLoaderTest extends VPStatSpec {

  dbTestController.clearAllTables()

  "CSVFileLoader" should "load a csv file and return the persisted records as a list" in {
    val list = CSVFileLoader.getListOfCSVFile(testProperties.getProperty("test_csv_table_path"))
    assert(list.length == 6)
  }

  dbTestController.clearAllTables()

}
