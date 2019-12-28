package de.berlin.vivepassion.io.database

import java.sql.{Connection, DriverManager}

/**
 * Controller to execute SQL functionality in the local database.
 *
 * @param dbUrl Path to the database
 *
 * @author Sebastian Führ
 * @version 0.1
 */
abstract class DBController(dbUrl: String) extends DBControllerInterface {

  @Override
  override def connect(): Connection = DriverManager.getConnection(dbUrl)

  @Override
  override def createTable(tableName: String, sqlStatement: String): Boolean = {
    val newSqlStatement: String = s"CREATE TABLE IF NOT EXISTS $tableName (\n" +
      sqlStatement +
      ");"
    connect().createStatement.execute(newSqlStatement)
    true
  }

  @Override
  override def clearTable(tableName: String): Boolean = {
    connect().createStatement.execute(s"DELETE FROM $tableName")
    true
  }

  @Override
  override def deleteTable(tableName: String): Boolean = {
    connect().createStatement.execute(s"DROP TABLE IF EXISTS $tableName") // TODO insert parenthesises after createStatement method
  }

  @Override
  override def deleteDatabase(): Boolean = { // TODO implement me
    deleteAllTables()
    false
  }

}
