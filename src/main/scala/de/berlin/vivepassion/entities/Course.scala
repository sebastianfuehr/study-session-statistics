package de.berlin.vivepassion.entities

import java.sql.ResultSet

/**
 * Entity which represents an university course.
 *
 * @param id Id of the course in the database.
 * @param name Name of the course.
 */
case class Course(id: Long, name: String) //TODO add ects points, description, professor
object Course extends Entity[Course] {

  /**
   * Converts a java ResultSet into a scala List[Course].
   * @param resultSet ResultSet to convert
   * @return List of courses
   */
  override def fromResultSet(resultSet: ResultSet): List[Course] = {
    new Iterator[Course] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext = resultSet.next()
      def next() = { // here a typecast happens
        val name = resultSet.getString("course_name")
        val id = resultSet.getLong("id")
        Course(id, name)
      }
    }.toList
  }

}