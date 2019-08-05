package de.berlin.vivepassion.entities

import java.sql.ResultSet
import java.time.{Instant, LocalDate, ZoneId}

/**
 * Entity which represents a study day.
 * @param id Id of the study day in the database.
 * @param date Date of the study day.
 * @param plannedStudyTime The planned study time.
 * @param comment An optional comment about what is planned or what has been done at that day.
 */
case class StudyDay(id: Long, date: LocalDate, plannedStudyTime: Int, comment: String) {

}
object StudyDay {

  /**
   * Converts a java ResultSet into a scala List[StudyDay].
   * @param resultSet ResultSet to convert.
   * @return List of study days.
   */
  def fromResultSet(resultSet: ResultSet): List[StudyDay] = {
    new Iterator[StudyDay] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext = resultSet.next()
      def next() = { // here a typecast happens
        val id = resultSet.getInt("id").toLong
        val date = Instant.ofEpochMilli(resultSet.getString("date").toLong).atZone(ZoneId.systemDefault()).toLocalDate
        val todoTime = resultSet.getInt("to_do")
        val comment = resultSet.getString("comment")
        StudyDay(id, date, todoTime, comment)
      }
    }.toList
  }

}