package de.berlin.vivepassion.io.database

/**
 * Provides functionality to manipulate a database with the following tables:
 * $tableNames
 *
 * @param dbUrl Path to the database
 *
 * @version 0.1
 * @author Sebastian Führ
 * @define tableNames COURSE, STUDY_FORM, SEMESTER, STUDY_DAY, STUDY_SESSION
 */
class VPStatsDBController(dbUrl: String) extends DBController(dbUrl) {

  /**
   * Creates the tables $tableNames for the local database if they don't already exist.
   *
   * @see DBController#createTable
   * @see #createCourseTable
   * @see #createStudyFormTable
   * @see #createSemesterTable
   * @see #createStudyDayTable
   * @see #createRecordTable
   */
  def createDatabase(): Boolean = {
    if (
      createCourseTable()
      && createStudyFormTable()
      && createSemesterTable()
      && createStudyDayTable()
      && createRecordTable()
    ) true
    else true
  }

  /**
   * Creates a table named RECORD in the local database.
   * @return Boolean if the operation was successful.
   * @see DBController#createTable
   */
  def createRecordTable(): Boolean = {
    createTable("STUDY_SESSION",
      "ID INTEGER PRIMARY KEY, \n" +
      "STUDY_DAY INTEGER NOT NULL, \n" +
      "FORM TEXT, \n" +
      "COURSE TEXT, \n" +
      "START_TIME INTEGER NOT NULL, \n" +
      "END_TIME INTEGER, \n" +
      "PAUSE INTEGER NOT NULL, \n" +
      "ALONE INTEGER NOT NULL, \n" +
      "COMMENT TEXT, \n" +
      "SEMESTER TEXT NOT NULL, \n" +
      "FOREIGN KEY(STUDY_DAY) REFERENCES STUDY_DAY(DATE), \n" +
      "FOREIGN KEY(form) REFERENCES STUDY_FORM(FORM_NAME), \n" +
      "FOREIGN KEY(course) REFERENCES course(COURSE_NAME), \n" +
      "FOREIGN KEY(semester) REFERENCES semester(SEMESTER_NAME)")
    true
  }

  /**
   * Creates a table named SEMESTER in the local database.
   * @return Boolean if the operation was successful.
   * @see DBController#createTable
   */
  def createSemesterTable(): Boolean = {
    createTable("SEMESTER",
      "ID INTEGER PRIMARY KEY, \n" +
      "SEMESTER_NAME TEXT NOT NULL, \n" +
      "START_DATE INTEGER NOT NULL, \n" +
      "END_DATE INTEGER NOT NULL, \n" +
      "CONSTRAINT unique_SEMESTER_NAME UNIQUE (SEMESTER_NAME), \n" +
      "CONSTRAINT unique_START_DATE UNIQUE (START_DATE), \n" +
      "CONSTRAINT unique_END_DATE UNIQUE (END_DATE)")
    true
  }

  /**
   * Creates a table named STUDY_DAY in the local database.
   * @return Boolean if the operation was successful.
   * @see DBController#createTable
   */
  def createStudyDayTable(): Boolean = {
    createTable("STUDY_DAY",
      "ID INTEGER PRIMARY KEY, \n" +
      "DATE INTEGER NOT NULL, \n" +
      "TO_DO INTEGER NOT NULL, \n" +
      "COMMENT TEXT NOT NULL, \n" +
      "CONSTRAINT unique_date UNIQUE (DATE)")
    true
  }

  /**
   * Creates a table named COURSE in the local database.
   * @return Boolean if the operation was successful.
   * @see DBController#createTable
   */
  def createCourseTable(): Boolean = {
    createTable("COURSE",
      "ID INTEGER PRIMARY KEY, \n" +
      "COURSE_NAME TEXT NOT NULL, " +
      "CONSTRAINT unique_COURSE_NAME UNIQUE (COURSE_NAME)")
    true
  }

  /**
   * Creates a table named STUDY_FORM in the local database.
   * @return Boolean if the operation was successful.
   * @see DBController#createTable
   */
  def createStudyFormTable(): Boolean = {
    createTable("STUDY_FORM",
      "ID INTEGER PRIMARY KEY, \n" +
      "FORM_NAME TEXT NOT NULL, " +
      "CONSTRAINT unique_STUDY_FORM UNIQUE (FORM_NAME)")
    true
  }

  /**
   * @inheritdoc
   * Clears the following database tables:
   * $tableNames
   */
  @Override
  override def clearAllTables(): Boolean = {
    clearTable("STUDY_SESSION")
    clearTable("STUDY_DAY")
    clearTable("SEMESTER")
    clearTable("COURSE")
    clearTable("STUDY_FORM")
    println("All tables have been cleared.") // define output stream
    true
  }



  /**
   * @inheritdoc
   * Tables are: $tableNames
   *
   * @see DBController#deleteTable
   */
  @Override
  override def deleteAllTables(): Boolean = {
    deleteTable("COURSE")
    deleteTable("STUDY_FORM")
    deleteTable("SEMESTER")
    deleteTable("STUDY_DAY")
    deleteTable("STUDY_SESSION")
  }

}