package de.berlin.vivepassion.io.database

import java.sql.ResultSet

/**
 * @author Sebastian Führ
 * @version 0.1
 */
trait DBRepositoryInterface {

  /**
   * Executes a query on the database and returns the results as a ResultSet.
   * @return SQL result set with the matching tuples.
   */
  def queryTableFor(tableName: String, attributeString: String): ResultSet

}
