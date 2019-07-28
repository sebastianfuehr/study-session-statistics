package de.berlin.vivepassion.io.database

import java.sql.ResultSet

/** Defines methods to interact with the database. */
class DBRepository(dbController: DBController) {

  /**
    * Retrieves all persisted study forms from the database table study_form.
    * @return List of study forms.
    */
  def getStudyForms: List[String] = getNamesFrom("study_form")

  /**
   * Retrieves all persisted courses from the database table course.
   * @return List of courses.
   */
  def getCourses: List[String] = getNamesFrom("course")

  /**
   * Retrieves all persisted semesters from the database table semester.
   * @return List of semesters.
   */
  def getSemesters: List[String] = getNamesFrom("semester")

  /**
   * Retrieves a list of names from a specified table with this column.
   * @param tableName Name of the table to get a list from
   * @return List of strings.
   */
  def getNamesFrom(tableName: String): List[String] = {
    val resultSet: ResultSet = dbController.connect.createStatement.executeQuery(s"SELECT name FROM $tableName")
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
    val sqlStatement = "INSERT INTO study_form(name) VALUES(?)";
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setString(1, form)
    prpstmt.executeUpdate
  }

  /**
   * Saves the course into the course database table.
   * @param course Name of the course.
   */
  def saveCourse(course: String): Unit = {
    val sqlStatement = "INSERT INTO course(name) VALUES(?)";
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setString(1, course)
    prpstmt.executeUpdate
  }

  /**
   * Saves the semester into the semester database table.
   * @param semester Name of the semester.
   */
  def saveSemester(semester: String): Unit = {
    val sqlStatement = "INSERT INTO semester(name) VALUES(?)";
    val prpstmt = dbController.connect.prepareStatement(sqlStatement)
    prpstmt.setString(1, semester)
    prpstmt.executeUpdate
  }

}
