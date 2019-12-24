package de.berlin.vivepassion.io.database

import java.sql.{Date, ResultSet, Timestamp}

import de.berlin.vivepassion.entities.{Record, Semester, StudyDay}

/**
 * Provides functionality to query a database with the following tables:
 * $tableNames
 *
 * @author Sebastian Führ
 * @version 0.1
 * @define tableNames COURSE, STUDY_FORM, SEMESTER, STUDY_DAY, STUDY_SESSION
 */
class VPStatsDBRepository(dbController: DBControllerInterface) extends DBRepository(dbController) {

  /**
   * Retrieves all persisted study sessions (record entity) from the database table STUDY_SESSION.
   * @return List of study sessions.
   * @see DBRepository#queryTableFor
   * @see Record#resultSetToList
   */
  def getRecords: List[Record] = {
    val attributes = "ID, FORM, COURSE, START_TIME, END_TIME, PAUSE, ALONE, COMMENT, SEMESTER"
    val resultSet: ResultSet = queryTableFor("STUDY_SESSION", attributes)
    Record.resultSetToList(resultSet)
  }

  /**
   * Retrieves all persisted study days from the database table STUDY_DAY.
   * @return List of study days.
   * @see DBRepository#queryTableFor
   * @see StudyDay#resultSetToList
   */
  def getStudyDays: List[StudyDay] = {
    val attributes = "ID, DATE, TO_DO, COMMENT"
    val resultSet: ResultSet = queryTableFor("STUDY_DAY", attributes)
    StudyDay.resultSetToList(resultSet)
  }

  /**
   * Retrieves all persisted study days from the database table STUDY_DAY as strings.
   * @return List of strings which hold the study days.
   * @see #getNamesFrom
   */
  def getStudyDaysAsStrings: List[String] = getNamesFrom("STUDY_DAY", "DATE")

  /**
   * Retrieves all persisted forms to study from the database table STUDY_FORM.
   * @return List of study forms.
   * @see #getNamesFrom
   */
  def getStudyForms: List[String] = getNamesFrom("STUDY_FORM", "FORM_NAME")

  /**
   * Retrieves all persisted courses from the database table COURSE.
   * @return List of courses.
   * @see #getNamesFrom
   */
  def getCourses: List[String] = getNamesFrom("COURSE", "COURSE_NAME")

  /**
   * Retrieves all persisted semesters from the database table SEMESTER.
   * @return List of semesters.
   * @see #getNamesFrom
   */
  def getSemesters: List[String] = getNamesFrom("SEMESTER", "SEMESTER_NAME")

  /**
   * Retrieves a list of strings from a specified table with this column.
   * @param tableName Name of the table to get a list from
   * @param nameString Column to be retrieved
   * @return List of strings.
   * @see DBRepository#queryTableFor
   */
  def getNamesFrom(tableName: String, nameString: String): List[String] = {
    val resultSet: ResultSet = queryTableFor(tableName, nameString)
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
    val prpstmt = dbController.connect().prepareStatement(sqlStatement)
    prpstmt.setString(1, form)
    prpstmt.executeUpdate
  }

  /**
   * Saves the course into the course database table.
   * @param course Name of the course.
   */
  def saveCourse(course: String): Unit = {
    val sqlStatement = "INSERT INTO course(course_name) VALUES(?)"
    val prpstmt = dbController.connect().prepareStatement(sqlStatement)
    prpstmt.setString(1, course)
    prpstmt.executeUpdate
  }

  /**
   * Saves the semester into the SEMESTER database table.
   * @param semester Name of the semester.
   */
  def saveSemester(semester: Semester): Unit = {
    val sqlStatement = "INSERT INTO SEMESTER (semester_name, start_date, end_date) VALUES(?, ? ,?)"
    val prpstmt = dbController.connect().prepareStatement(sqlStatement)
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
    val prpstmt = dbController.connect().prepareStatement(sqlStatement)
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
    println(s"Save study session in DB: $record")
    val sqlStatement = "INSERT INTO STUDY_SESSION(" +
      "study_day, form, course, start_time, end_time, pause, alone, comment, semester" +
      ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
    val prpstmt = dbController.connect().prepareStatement(sqlStatement)
    prpstmt.setDate(1, Date.valueOf(record.getDate))

    record.form match {
      case Some(form) => prpstmt.setString(2, form)
      case None       => prpstmt.setNull(2, java.sql.Types.VARCHAR)
    }

    record.course match {
      case Some(course) => prpstmt.setString(3, course)
      case None         => prpstmt.setNull(3, java.sql.Types.VARCHAR)
    }

    prpstmt.setInt(4, (Timestamp.valueOf(record.startTime).getTime / 1000).toInt)

    record.endTime match {
      case Some(endTime) => prpstmt.setInt(5, (Timestamp.valueOf(endTime).getTime / 1000).toInt)
      case None          => prpstmt.setNull(5, java.sql.Types.INTEGER)
    }

    prpstmt.setInt(6, record.pause)
    val aloneInt = if (record.alone) 1 else 0
    prpstmt.setInt(7, aloneInt)

    record.comment match {
      case Some(comment) => prpstmt.setString(8, comment)
      case None          => prpstmt.setNull(8, java.sql.Types.VARCHAR)
    }

    prpstmt.setString(9, record.semester)
    prpstmt.execute()
  }

  def getLastRecord: Record = {
    val sqlStatement = "id, form, course, start_time, end_time, pause, alone, comment, SEMESTER"
    val resultList = Record.resultSetToList(queryTableFor("RECORD", sqlStatement))
    resultList.reduce((acc, elem) => if (elem.startTime.isAfter(acc.startTime)) elem else acc)
  }

}
