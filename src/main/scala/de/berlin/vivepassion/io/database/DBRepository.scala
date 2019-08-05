package de.berlin.vivepassion.io.database

import java.sql.{Date, ResultSet, Timestamp}

import de.berlin.vivepassion.VPStats
import de.berlin.vivepassion.controller.{CourseController, RecordController, SemesterController, StudyFormController}
import de.berlin.vivepassion.entities.{Record, Semester, StudyDay}
import de.berlin.vivepassion.io.CSVFileLoader

/** Defines methods to interact with the database. */
class DBRepository(dbController: DBController) {

  val courseController = new CourseController(this)
  val recordController = new RecordController(this)
  val semesterController = new SemesterController(this)
  val studyFormController = new StudyFormController(this)

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
    println(s"Save Record in DB: $record")
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
    prpstmt.execute()
  }

  def getLastRecord(): Record = {
    val resultList = Record.fromResultSet(queryDatabaseFor("SELECT * FROM record"))
    resultList.reduce((acc, elem) => if (elem.startTime.isAfter(acc.startTime)) elem else acc)
  }

  /**
   * Read all lines of a csv file and save then one by one in a database.
   * @param csvPath Path of the csv file to be imported.
   */
  def importCsvIntoDatabase(csvPath: String, semesterName: String) = {
    val list: List[Record] = CSVFileLoader.getListOfCSVFile(csvPath)

    if (VPStats.debugMode) println(s"Importing ${list.length} elements of semester $semesterName")

    val firstDay = list.map(rec => rec.getDate)
      .reduce((acc, elem) => if (elem.isBefore(acc)) elem else acc)
    val lastDay = list.map(rec => rec.getDate)
      .reduce((acc, elem) => if (elem.isAfter(acc)) elem else acc)
    saveSemester(Semester(-1, semesterName, firstDay, lastDay))

    for (element <- list) {
      courseController.createCourseIfNotExists(element.course)
      studyFormController.createStudyFormIfNotExists(element.form)
      saveRecord(element)
    }
  }

}
