package de.berlin.vivepassion.io.database

import java.sql.{Connection, DriverManager, Statement}

import de.berlin.vivepassion.VPSConfiguration

object DBInit {

  def createRecordTable: Unit = { // TODO
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS records (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "form TEXT, \n" +
      "course TEXT, \n" +
      "start_time TEXT, \n" +
      "end_time TEXT, \n" +
      "pause INTEGER, \n" +
      "alone INTEGER NOT NULL, \n" +
      "comment TEXT\n" +
      ");"
  }

  def createSemesterTable: Unit = { // TODO

  }

  def createLearnDayTable: Unit = { // TODO

  }

  def createCourseTable: Unit = { // TODO
    val sqlStatement: String = ""
  }

  def createStudyFormTable: Unit = { // TODO
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS StudyForm (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "name TEXT NOT NULL\n" +
      ");"

    val connection: Connection = DriverManager.getConnection(VPSConfiguration.properties.getProperty("db_url"))
    val statement: Statement = connection.createStatement()
    statement.execute(sqlStatement)
  }

}
