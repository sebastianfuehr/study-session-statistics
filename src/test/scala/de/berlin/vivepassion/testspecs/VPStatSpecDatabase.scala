package de.berlin.vivepassion.testspecs

import de.berlin.vivepassion.entities.Record

/**
 * Like VPStatSpec but
 */
class VPStatSpecDatabase extends VPStatSpec {

  dbTestController.clearAllTables()

  dbTestRepository.importCsvIntoDatabase(testProperties.getProperty("test_csv_table_path"), "WS18/19")
  val testStudySessions: List[Record] = dbTestRepository.getRecords()

}
