package de.berlin.vivepassion.io.database

import java.sql.ResultSet

/**
 * Defines methods to query the local database.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
abstract class DBRepository(dbController: DBControllerInterface) extends DBRepositoryInterface {

  @Override
  override def queryTableFor(tableName:String, attributesString: String): ResultSet = {
    val sqlStatement: String = "SELECT " + attributesString + " FROM " + tableName
    dbController.connect().createStatement.executeQuery(sqlStatement);
  }

}
