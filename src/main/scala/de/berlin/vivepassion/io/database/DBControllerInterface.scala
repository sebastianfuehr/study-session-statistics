package de.berlin.vivepassion.io.database

import java.sql.Connection

/**
 * Service to execute SQL functionality in a database with java.sql.*
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait DBControllerInterface {

  /**
   * Retrieves the connection to the local database.
   * @return SQL connection instance.
   */
  def connect(): Connection

  /**
   * Create a table in the database if it doesn't already exists.
   *
   * @param tableName Name of the new database table
   * @param sqlStatement SQL statement inside the parenthesises. E.g. "ID INTEGER PRIMARY KEY"
   * @return Boolean if the operation was successfully finished.
   */
  def createTable(tableName: String, sqlStatement: String): Boolean

  /**
   * Clears the specified table.
   * @param tableName Table to be cleared.
   * @return Boolean if the operation was successfully finished.
   */
  def clearTable(tableName: String): Boolean

  /**
   * Delete the content of all tables of the database.
   * @return Boolean if the operation was successfully finished.
   */
  def clearAllTables(): Boolean

  /**
   * Delete a specific table of the database.
   * @param tableName Table to be deleted
   * @return Boolean if the operation was successfully finished.
   */
  def deleteTable(tableName: String): Boolean

  /**
   * Delete all tables of the database.
   * @return Boolean if the operation was successfully finished.
   */
  def deleteAllTables(): Boolean

  /**
   * Delete the database file.
   * @return Boolean if the operation was successfully finished.
   */
  def deleteDatabase(): Boolean

}
