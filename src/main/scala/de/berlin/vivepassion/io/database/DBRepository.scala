package de.berlin.vivepassion.io.database

import java.sql.{Date, ResultSet, Timestamp}

import de.berlin.vivepassion.entities.{Record, Semester, StudyDay}

/** Defines methods to interact with the database. */
class DBRepository(dbController: DBController) {

  /** Executes a query on the database and returns the results as a ResultSet. */
  def queryDatabaseFor(sqlStatement: String): ResultSet = {
    dbController.connect.createStatement.executeQuery(sqlStatement);
  }

  // get entities methods --------------------------------------------------------------
  /**
   * Retrieves all persisted study sessions (record entity) from the database table record.
   * @return List of study sessions.
   */
  def getRecords: List[Record] = {
    val resultSet: ResultSet = queryDatabaseFor("SELECT * FROM record")
    Record.fromResultSet(resultSet)
  }

  /**
   * Retrieves all persisted study days from the database table study_day.
   * @return List of study days.
   */
  def getStudyDays: List[StudyDay] = {
    val resultSet: ResultSet = queryDatabaseFor("SELECT * FROM study_day")
    StudyDay.fromResultSet(resultSet)
  }

  /**
    * Retrieves all persisted study forms from the database table study_form.
    * @return List of study forms.
    */
  def getStudyForms: List[String] = getNamesFrom("study_form", "form_name")

  /**
   * Retrieves all persisted courses from the database table course.
   * @return List of courses.
   */
  def getCourses: List[String] = getNamesFrom("course", "course_name")

  /**
   * Retrieves all persisted semesters from the database table studyForm.
   * @return List of semesters.
   */
  def getSemesters: List[String] = getNamesFrom("semester", "semester_name")

  /**
   * Retrieves a list of names from a specified table with this column.
   * @param tableName Name of the table to get a list from
   * @return List of strings.
   */
  def getNamesFrom(tableName: String, nameString: String): List[String] = {
    val resultSet: ResultSet = queryDatabaseFor(s"SELECT $nameString FROM $tableName")
    new Iterator[String] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext = resultSet.next()
      def next() = resultSet.getString(1)
    }.toList
  }

  // saving entities in the database methods -----------------------------------------------
  /**
   * Saves the form of studying into the study_form database table.
   * @param form The study form to be saved.
   */
  def saveStudyForm(form: String): Unit = {
    val sqlStatement = "INSERT INTO study_form(form_name) VALUES(?)"
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setString(1, form)
    prpstmt.executeUpdate
  }

  /**
   * Saves the course into the course database table.
   * @param course Name of the course.
   */
  def saveCourse(course: String): Unit = {
    val sqlStatement = "INSERT INTO course(course_name) VALUES(?)"
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setString(1, course)
    prpstmt.executeUpdate
  }

  /**
   * Saves the studyForm into the studyForm database table.
   * @param semester Name of the studyForm.
   */
  def saveSemester(semester: Semester): Unit = {
    val sqlStatement = "INSERT INTO semester(semester_name, start_date, end_date) VALUES(?, ? ,?)"
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setString(1, semester.name)
    prpstmt.setDate(2, Date.valueOf(semester.start))
    prpstmt.setDate(3, Date.valueOf(semester.end))
    prpstmt.executeUpdate
  }

  /**
   * Saves the study day into the study_day database table.
   * @param studyDay StudyDay entity to be saved.
   */
  def saveStudyDay(studyDay: StudyDay): Unit = {
    val sqlStatement = "INSERT INTO study_day(date, to_do, comment) VALUES(?, ?, ?)"
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setDate(1, Date.valueOf(studyDay.date))
    prpstmt.setInt(2, studyDay.plannedStudyTime)
    prpstmt.setString(3, studyDay.comment)
    prpstmt.execute()
  }

  /**
   * Saves the study session into the record database table.
   * @param record Record entity to be saved.
   */
  def saveRecord(record: Record): Unit = {
    val sqlStatement = "INSERT INTO record(" +
      "study_day, form, course, start_time, end_time, pause, alone, comment, semester" +
      ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setDate(1, Date.valueOf(record.getDate))
    prpstmt.setString(2, if (record.form == null) null else record.form)
    prpstmt.setString(3, if (record.course == null) null else record.course)
    prpstmt.setInt(4, (Timestamp.valueOf(record.startTime).getTime / 1000).toInt)

    if (record.endTime == null) prpstmt.setNull(5, java.sql.Types.INTEGER)
    else prpstmt.setInt(5, (Timestamp.valueOf(record.endTime).getTime / 1000).toInt)

    prpstmt.setInt(6, record.pause)
    val aloneInt = if (record.alone) 1 else 0
    prpstmt.setInt(7, aloneInt)
    prpstmt.setString(8, record.comment)
    prpstmt.setString(9, record.semester)
    prpstmt.execute
  }

  def getLastRecord(): Record = {
    val resultList = Record.fromResultSet(queryDatabaseFor("SELECT * FROM record"))
    resultList.reduce((acc, elem) => if (elem.startTime.isAfter(acc.startTime)) elem else acc)
  }

}
