package de.berlin.vivepassion.io.database

import java.sql.{Connection, DriverManager}

/** Controller to interact with the local database. */
class DBController(dbUrl: String) {

  /**
   * Retrieves the connection to the local database.
   * @return SQL connection instance.
   */
  def connect: Connection = DriverManager.getConnection(dbUrl)

  /** Creates all tables for the database if they don't already exist.  */
  def createDatabase: Unit = {
    createCourseTable
    createStudyFormTable
    createSemesterTable
    createStudyDayTable
    createRecordTable
  }

  // create table methods --------------------------------------------------------------------
  def createRecordTable: Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS record (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "form TEXT, \n" +
      "course TEXT, \n" +
      "start_time INTEGER, \n" +
      "end_time INTEGER, \n" +
      "pause INTEGER, \n" +
      "alone INTEGER NOT NULL, \n" +
      "comment TEXT, \n" +
      "FOREIGN KEY(form) REFERENCES study_form(name), \n" +
      "FOREIGN KEY(course) REFERENCES course(name)" +
      ");"
    connect.createStatement.execute(sqlStatement)
  }

  def createSemesterTable: Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS semester (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "name TEXT NOT NULL, \n" +
      "CONSTRAINT unique_semester_name UNIQUE (name)" +
      ");"
    connect.createStatement.execute(sqlStatement)
  }

  def createStudyDayTable: Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS study_day (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "date INTEGER NOT NULL, \n" +
      "to_do INTEGER, \n" +
      "comment TEXT, \n" +
      "CONSTRAINT unique_date UNIQUE (date)" +
      ");"
    connect.createStatement.execute(sqlStatement)
  }

  def createCourseTable: Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS course (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "name TEXT NOT NULL, " +
      "CONSTRAINT unique_course_name UNIQUE (name)\n" +
      ");"
    connect.createStatement.execute(sqlStatement)
  }

  def createStudyFormTable: Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS study_form (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "name TEXT NOT NULL, " +
      "CONSTRAINT unique_study_form UNIQUE (name)\n" +
      ");"
    connect.createStatement.execute(sqlStatement)
  }

  /** Clears the database tables study_day, semester, course, study_form and record. */
  def clearAllTables: Unit = {
    clearTable("record")
    clearTable("study_day")
    clearTable("semester")
    clearTable("course")
    clearTable("study_form")
  }

  /**
   * Clears the specified table.
   * @param tableName Table to be cleared.
   */
  def clearTable(tableName: String): Unit = connect.createStatement.execute(s"DELETE FROM $tableName")

}
