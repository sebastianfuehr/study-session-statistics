package de.berlin.vivepassion.entities

import java.sql.ResultSet
import java.time.{Instant, LocalDate, ZoneId}

/**
 * Entity which represents a university semester with a variable lengths.
 *
 * @param id Id of the semester in the database.
 * @param name Name of the semester.
 * @param start Date of the first day of the semester.
 * @param end Date of the last day of the semester.
 */
case class Semester(id: Long, name: String, start: LocalDate, end: LocalDate) {

}
object Semester extends Entity[Semester] {

  /**
   * Converts a java ResultSet into a scala List[Semester].
   * @param resultSet ResultSet to convert
   * @return List of semesters
   */
  override def fromResultSet(resultSet: ResultSet): List[Semester] = {
    new Iterator[Semester] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext = resultSet.next()
      def next() = { // here a typecast happens
        val name = resultSet.getString("semester_name")
        val startDate = Instant.ofEpochMilli(resultSet.getInt("start_date").toLong)
          .atZone(ZoneId.systemDefault()).toLocalDate
        val endDate = Instant.ofEpochMilli(resultSet.getInt("end_date").toLong)
          .atZone(ZoneId.systemDefault()).toLocalDate
        val id = resultSet.getLong("id")
        Semester(id, name, startDate, endDate)
      }
    }.toList
  }

}
