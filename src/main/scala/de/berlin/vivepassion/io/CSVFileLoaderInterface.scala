package de.berlin.vivepassion.io

import de.berlin.vivepassion.controller.EntityControllerInterface
import de.berlin.vivepassion.entities.Record

/**
 * Define functionality to interact with csv files.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait CSVFileLoaderInterface {

  /**
   * Reads all lines of the csv file except the first line which is supposed to hold the column headings.
   * @param csvPath Path of the csv file to be imported.
   * @return List of learn sessions in form of the Record entity.
   */
  def getListOfCSVFile(csvPath: String): List[Record]

  /**
   * Read all lines of a csv file and save then one by one in a database.
   * @param csvPath Path of the csv file to be imported.
   * @return Boolean if the operation was successful.
   */
  def importCsvIntoDatabase(csvPath: String, entityController: EntityControllerInterface): Boolean

}
