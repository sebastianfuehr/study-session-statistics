package de.berlin.vivepassion.io.database

import java.sql.{Connection, DriverManager}

/** Controller to interact with the local database. */
class DBController(dbUrl: String) {

  /**
   * Retrieves the connection to the local database.
   * @return SQL connection instance.
   */
  def connect(): Connection = DriverManager.getConnection(dbUrl)

  /** Creates all tables for the database if they don't already exist.  */
  def createDatabase(): Unit = {
    createCourseTable()
    createStudyFormTable()
    createSemesterTable()
    createStudyDayTable()
    createRecordTable()
  }

  // create table methods --------------------------------------------------------------------
  def createRecordTable(): Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS record (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "study_day INTEGER NOT NULL, \n" +
      "form TEXT NOT NULL, \n" +
      "course TEXT NOT NULL, \n" +
      "start_time INTEGER NOT NULL, \n" +
      "end_time INTEGER, \n" +
      "pause INTEGER NOT NULL, \n" +
      "alone INTEGER NOT NULL, \n" +
      "comment TEXT NOT NULL, \n" +
      "semester TEXT NOT NULL, \n" +
      "FOREIGN KEY(study_day) REFERENCES study_day(date), \n" +
      "FOREIGN KEY(form) REFERENCES study_form(form_name), \n" +
      "FOREIGN KEY(course) REFERENCES course(course_name), \n" +
      "FOREIGN KEY(semester) REFERENCES semester(semester_name)" +
      ");"
    connect().createStatement.execute(sqlStatement)
  }

  def createSemesterTable(): Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS semester (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "semester_name TEXT NOT NULL, \n" +
      "start_date INTEGER NOT NULL, \n" +
      "end_Date INTEGER NOT NULL, \n" +
      "CONSTRAINT unique_semester_name UNIQUE (semester_name), \n" +
      "CONSTRAINT unique_start_date UNIQUE (start_date), \n" +
      "CONSTRAINT unique_end_date UNIQUE (end_date)" +
      ");"
    connect().createStatement.execute(sqlStatement)
  }

  def createStudyDayTable(): Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS study_day (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "date INTEGER NOT NULL, \n" +
      "to_do INTEGER NOT NULL, \n" +
      "comment TEXT NOT NULL, \n" +
      "CONSTRAINT unique_date UNIQUE (date)" +
      ");"
    connect().createStatement.execute(sqlStatement)
  }

  def createCourseTable(): Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS course (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "course_name TEXT NOT NULL, " +
      "CONSTRAINT unique_course_name UNIQUE (course_name)\n" +
      ");"
    connect().createStatement.execute(sqlStatement)
  }

  def createStudyFormTable(): Unit = {
    val sqlStatement: String = "CREATE TABLE IF NOT EXISTS study_form (\n" +
      "id INTEGER PRIMARY KEY, \n" +
      "form_name TEXT NOT NULL, " +
      "CONSTRAINT unique_study_form UNIQUE (form_name)\n" +
      ");"
    connect().createStatement.execute(sqlStatement)
  }

  /** Clears the database tables study_day, studyForm, course, study_form and record. */
  def clearAllTables(): Unit = {
    clearTable("record")
    clearTable("study_day")
    clearTable("semester")
    clearTable("course")
    clearTable("study_form")
    println("All tables have been cleared.")
  }

  /**
   * Clears the specified table.
   * @param tableName Table to be cleared.
   */
  def clearTable(tableName: String): Unit = connect().createStatement.execute(s"DELETE FROM $tableName")

}
