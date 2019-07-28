package de.berlin.vivepassion.io.database

import java.sql.{Connection, DriverManager, ResultSet}

import de.berlin.vivepassion.VPSConfiguration

/** Controller to interact with the local database. */
object DBConnector {

  /**
    * Retrieves the connection to the local database.
    * @return SQL connection instance.
    */
  def connect: Connection = {
    // db parameters
    val url = VPSConfiguration.properties.getProperty("db_url")
    // create a connection to the database
    DriverManager.getConnection(url)
  }

  /**
    * Retrieves all persisted study forms of the database.
    * @return List of study forms.
    */
  def getStudyForms: List[String] = {
    val sqlStatement = "SELECT name FROM StudyForm"
    val result: ResultSet = connect.createStatement.executeQuery(sqlStatement)

  }

}
