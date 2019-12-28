package de.berlin.vivepassion.entities

import java.sql.ResultSet
import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, ZoneId}

import de.berlin.vivepassion.VPSConfiguration

/**
 * Entity which represents a study day.
 * @param id Id of the study day in the database.
 * @param date Date of the study day.
 * @param plannedStudyTime The planned study time.
 * @param comment An optional comment about what is planned or what has been done at that day.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
case class StudyDay(id: Long, date: LocalDate, plannedStudyTime: Int, comment: String) extends Entity[StudyDay] {

  /**
   * @inheritdoc
   * @return Saved instance of T.
   */
  @Override
  override def getEntityClass: StudyDay = this

  def getDateString: String = date.format(VPSConfiguration.getDateFormatter)

}
object StudyDay extends EntityObjectInterface[StudyDay] {

  /**
   * Converts a java ResultSet into a scala List[StudyDay].
   * @param resultSet ResultSet to convert.
   * @return List of study days.
   */
  @Override
  override def resultSetToList(resultSet: ResultSet): List[StudyDay] = {
    new Iterator[StudyDay] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext: Boolean = resultSet.next()
      def next(): StudyDay = { // here a typecast happens
        val id = resultSet.getInt("id").toLong
        val date = Instant.ofEpochMilli(resultSet.getString("date").toLong).atZone(ZoneId.systemDefault()).toLocalDate
        val todoTime = resultSet.getInt("to_do")
        val comment = resultSet.getString("comment")
        StudyDay(id, date, todoTime, comment)
      }
    }.toList
  }

  /**
   * Factory method to create a StudyDay instance with only a date string as parameter.
   * @param dateString String which represents a date.
   * @return New instance of a StudyDay.
   * @note 'id' will be -1, 'plannedStudyTime' will be 0 and 'comment' will be empty.
   */
  def makeStudyDay(dateString: String): StudyDay = {
    val newDate: LocalDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(VPSConfiguration.properties.getProperty("default_date_format")))
    StudyDay(-1, newDate, 0, "")
  }

}