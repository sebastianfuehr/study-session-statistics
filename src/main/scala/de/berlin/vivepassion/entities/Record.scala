package de.berlin.vivepassion.entities

import java.io.FileInputStream
import java.sql.ResultSet
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Instant, LocalDate, LocalDateTime, ZoneId}
import java.util.Properties

import de.berlin.vivepassion.{VPSConfiguration, VPStats}
import de.berlin.vivepassion.io.CSVLearnSessionFormat

/**
  * Entity class to describe a learn session as detailed as possible.
  * @param form The form of learning (e.g. doing homework or using flashcards).
  * @param course The university course.
  * @param startTime The time the learn session started.
  * @param endTime The time the learn session ended.
  * @param pause The amount of minutes which where used for regeneration, free time etc.
  * @param alone Did one work alone during this learn session or with other students?
  * @param comment What especially has been done during the study session?
  * @param id Identifier of the specific record entity.
  */
case class Record(form: String, course: String, startTime: LocalDateTime, endTime: LocalDateTime,
                  pause: Int, alone: Boolean, comment: String, id: Long, semester: String) {

  /**
    * @return The date of the learn session.
    */
  def getDate: LocalDate = startTime.toLocalDate
  def getDateString: String = startTime.format(Record.dateFormatter)

  def getStartTimeString: String = startTime.format(Record.timeFormatter)
  def getEndTimeString: String = endTime.format(Record.timeFormatter)

  /**
    * @return The length of the learn session in minutes.
    */
  def getSessionLength: Long = startTime.until(endTime, ChronoUnit.MINUTES) - pause

  override def toString: String = {
    s"[$id] $getDateString from $getStartTimeString to $getEndTimeString -> ${getSessionLength}m"
  }

}
object Record {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

  /*
   * Initialisation of different formats for date and time for the record entity.
   */
  val timeFormatStr: String = properties.getProperty("default_time_format")
  val dateFormatStr: String = properties.getProperty("default_date_format")
  val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatStr)
  val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(timeFormatStr)
  val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
    s"$dateFormatStr'T'$timeFormatStr"
  )

  /**
    * This methods requests the default date and time format strings from the vivepassionstats.properties and
    * calls the overloaded fromLine(...) method with these global default values.
    * @param line String to parse to a new line.
    * @return New instance of Record.
    */
  def fromLine(line: String, id: Long): Record = {
    val defaultDateTimeFormat = (
        properties.getProperty("default_date_format")
        +"'T'"
        +properties.getProperty("default_time_format")
      )
    val defaultCSVLearnSessionFileFormat = new CSVLearnSessionFormat(',')
    fromLine(line, defaultDateTimeFormat, defaultCSVLearnSessionFileFormat, id)
  }

  /**
    *
    * @param line String to parse.
    * @param dateTimeFormat Format of the date and time columns.
    * @return New instance of Record.
    */
  // TODO Methoden zum parsen bereitstellen (mit Fehlerüberprüfung)
  def fromLine(line: String, dateTimeFormat: String, csv: CSVLearnSessionFormat, id: Long): Record = {
    val recordString = line.split(",", -1)
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat)

    if (VPStats.debugMode) println(s"Trying to parse ${recordString(csv.dateColumn)}...")
    val startTime: LocalDateTime = LocalDateTime.parse(
      recordString(csv.dateColumn)+"T"+recordString(csv.startTimeColumn), dateTimeFormatter)
    val endTime: LocalDateTime = LocalDateTime.parse(
      recordString(csv.dateColumn)+"T"+recordString(csv.endTimeColumn), dateTimeFormatter)

    var pause: Int = 0
    if (recordString(csv.pauseColumn).length != 0) pause = Integer.parseInt(recordString(csv.pauseColumn))

    val isAlone: Boolean = getIsAloneBoolean(recordString(csv.aloneColumn), csv.aloneKeyWord)

    val course = recordString(csv.courseColumn)

    val comment: String = recordString(csv.commentColumn)

    val semester: String = properties.getProperty("current_semester")

    Record(recordString(csv.formColumn), course, startTime, endTime, pause, isAlone, comment, id, semester)
  }

  /**
    * Compares the given String with the keyword which represents the keyword which was used in the csv table in the
    * alone column.
    * @param isAlone String to be compared.
    * @param aloneKeyWord String which represents the alone column in the csv table.
    * @return
    */
  def getIsAloneBoolean(isAlone: String, aloneKeyWord: String): Boolean = isAlone.equals(aloneKeyWord)

  /**
   * Converts a java ResultSet into a scala List[Record].
   * @param resultSet ResultSet to convert.
   * @return List of records.
   */
  def fromResultSet(resultSet: ResultSet): List[Record] = {
    new Iterator[Record] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext = resultSet.next()
      def next() = { // here a typecast happens
        val form = resultSet.getString("form")
        val course = resultSet.getString("course")
        val startTime = Instant.ofEpochMilli(resultSet.getInt("start_time").toLong)
          .atZone(ZoneId.systemDefault()).toLocalDateTime
        val endTime = Instant.ofEpochMilli(resultSet.getInt("end_time").toLong)
          .atZone(ZoneId.systemDefault()).toLocalDateTime
        val pause = resultSet.getInt("pause")
        val alone = resultSet.getInt("alone") == 1
        val comment = resultSet.getString("comment")
        val id = resultSet.getInt("id").toLong
        val semester = resultSet.getString("semester")
        Record(form, course, startTime, endTime, pause, alone, comment, id, semester)
      }
    }.toList
  }

}
