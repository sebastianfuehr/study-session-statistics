package de.berlin.vivepassion.entities

import java.io.FileInputStream
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Properties

import de.berlin.vivepassion.io.CSVLearnSessionFormat

/**
  * Entity class to describe a learn session as detailed as possible.
  * @param form The form of learning (e.g. doing homework or using flashcards).
  * @param course The university course.
  * @param startTime The time the learn session started.
  * @param endTime The time the learn session ended.
  * @param pause The amount of minutes which where used for regeneration, free time etc.
  * @param remainingTime How many minutes more should have been used for learning but didn't.
  * @param alone Did one work alone during this learn session or with other students?
  */
case class Record(form: String, course: String, startTime: LocalDateTime,
             endTime: LocalDateTime, pause: Int, remainingTime: Int, alone: Boolean) {

  /**
    * @return The date of the learn session.
    */
  def getDate: LocalDate = startTime.toLocalDate

  /**
    * @return The length of the learn session in minutes.
    */
  def getSessionLength: Long = startTime.until(endTime, ChronoUnit.MINUTES)

}
object Record {

  val PROPERTIES_PATH = "resources/vivepassionstats.properties"

  var properties: Properties = new Properties()
  properties.load(new FileInputStream(PROPERTIES_PATH))

  /**
    * This methods requests the default date and time format strings from the vivepassionstats.properties and
    * calls the overloaded fromLine(...) method with these global default values.
    * @param line String to parse to a new line.
    * @return New instance of Record.
    */
  def fromLine(line: String): Record = {
    val default_date_time_format = (
        properties.getProperty("default_date_format")
        +"'T'"
        +properties.getProperty("default_time_format")
      )
    val default_csv_learn_session_file_format = new CSVLearnSessionFormat(',')
    fromLine(line, default_date_time_format, default_csv_learn_session_file_format)
  }

  /**
    *
    * @param line String to parse.
    * @param date_time_format Format of the date and time columns.
    * @return New instance of Record.
    */
  // TODO Methoden zum parsen bereitstellen
  def fromLine(line: String, date_time_format: String, csv: CSVLearnSessionFormat): Record = {
    val recordString = line.split(",")
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(date_time_format)

    val startTime: LocalDateTime = LocalDateTime.parse(
      recordString(csv.dateColumn)+"T"+recordString(csv.startTimeColumn), dateTimeFormatter)
    val endTime: LocalDateTime = LocalDateTime.parse(
      recordString(csv.dateColumn)+"T"+recordString(csv.endTimeColumn), dateTimeFormatter)

    var pause: Int = 0
    if (recordString(csv.pauseColumn).length != 0) pause = Integer.parseInt(recordString(4))

    val remaining: Int = Integer.parseInt(recordString(csv.remainingTimeColumn))

    val isAlone: Boolean = getIsAloneBoolean(recordString(csv.aloneColumn), csv.aloneKeyWord)

    var course: String = ""
    if (recordString.length > 8) course = recordString(csv.courseColumn)

    Record(recordString(csv.formColumn), course, startTime, endTime, pause, remaining, isAlone)
  }

  /**
    * Compares the given String with the keyword which represents the keyword which was used in the csv table in the
    * alone column.
    * @param isAlone String to be compared.
    * @param aloneKeyWord String which represents the alone column in the csv table.
    * @return
    */
  def getIsAloneBoolean(isAlone: String, aloneKeyWord: String): Boolean = if (isAlone.equals(aloneKeyWord)) true else false

}
