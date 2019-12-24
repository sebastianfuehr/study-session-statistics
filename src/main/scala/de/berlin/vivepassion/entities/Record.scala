package de.berlin.vivepassion.entities

import java.sql.ResultSet
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Instant, LocalDate, LocalDateTime, ZoneId}
import java.util.Properties

import de.berlin.vivepassion.io.CSVLearnSessionFormat
import de.berlin.vivepassion.{VPSConfiguration, VPStats}

/**
  * Entity class to describe a study session.
  * @param id Identifier of the specific record entity in the database.
  * @param form Optional of the form of learning (e.g. doing homework or using flashcards).
  * @param course Optional of the university course.
  * @param startTime The time the learn session started.
  * @param endTime Optional of the time the learn session ended.
  * @param pause The amount of minutes which where used for regeneration, free time etc.
  * @param alone Did one work alone{if (comment.length > 0) comment else "-"} during this learn session or with other students?
  * @param comment Optional of a comment: What especially has been done during the study session?
  *
  * @author Sebastian Führ
  * @version 0.1
  */
case class Record(id: Long,
                  form: Option[String],
                  course: Option[String],
                  startTime: LocalDateTime,
                  endTime: Option[LocalDateTime],
                  pause: Int,
                  alone: Boolean,
                  comment: Option[String],
                  semester: String) extends Entity[Record] {
  require({
    endTime match {
      case Some(endTime) =>
        if (endTime.isBefore(startTime)) false
        else true
      case None          => true
    }
  }, "An end time can't be before start time!")

  /**
    * @return The date of the learn session.
    */
  def getDate: LocalDate = startTime.toLocalDate
  def getDateString: String = startTime.format(Record.dateFormatter)

  def getStartTimeString: String = startTime.format(Record.timeFormatter)
  def getEndTimeString: String = endTime match {
    case Some(endTime) => endTime.format(Record.timeFormatter)
    case None          => " - "
  }

  def getCommentString: String = {
    comment match {
      case Some(comment) => comment
      case None          => "-"
    }
  }

  def getStudyFormString: String = {
    form match {
      case Some(form) => form
      case None       => "-"
    }
  }

  def getCourseString: String = {
    course match {
      case Some(course) => course
      case None         => "-"
    }
  }

  /**
    * @return The length of the learn session in minutes.
    */
  def getSessionLength: Int = {
    endTime match {
      case Some(endTime) => (startTime.until(endTime, ChronoUnit.MINUTES) - pause).toInt
      case None          => // session length until now
        (startTime.until(LocalDateTime.now(), ChronoUnit.MINUTES) - pause).toInt
    }
  }

  /** @return Returns a string representation of the study session. */
  @Override
  override def toString: String = {
    s"[$id] $getDateString - $getStartTimeString to $getEndTimeString " +
      s"| $getSessionLength min (- $pause min) | " +
      s"${if (alone) VPSConfiguration.langProps.getProperty("alone")
      else VPSConfiguration.langProps.getProperty("group")}, " +
      s"$getCourseString, $getStudyFormString, $getCommentString"
  }

  /**
   * @inheritdoc
   * @return Saved instance of T.
   */
  @Override
  override def getEntityClass: Record = this

}
object Record extends EntityObjectInterface[Record] {

  val properties: Properties = VPSConfiguration.properties

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

    val comment: Option[String] =
      if (recordString(csv.commentColumn).isEmpty) None
      else Some(recordString(csv.commentColumn))

    val semester: String = properties.getProperty("current_semester")

    Record(id,
      Some(recordString(csv.formColumn)),
      Some(course),
      startTime,
      Some(endTime),
      pause,
      isAlone,
      comment,
      semester)
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
   * @param resultSet ResultSet to convert
   * @return List of records
   */
  @Override
  override def resultSetToList(resultSet: ResultSet): List[Record] = {
    new Iterator[Record] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext: Boolean = resultSet.next()
      def next(): Record = { // here a typecast happens
        val form = resultSet.getString("form")
        val course = resultSet.getString("course")
        val startTime = Instant.ofEpochSecond(resultSet.getInt("start_time").toLong)
          .atZone(ZoneId.systemDefault()).toLocalDateTime
        val endTime = Instant.ofEpochSecond(resultSet.getInt("end_time").toLong)
          .atZone(ZoneId.systemDefault()).toLocalDateTime
        val pause = resultSet.getInt("pause")
        val alone = resultSet.getInt("alone") == 1
        //val comment = if (resultSet.getString("comment") == null) None else Some(resultSet.getString("comment"))
        val comment = Option(resultSet.getString("comment"))
        val id = resultSet.getInt("id").toLong
        val semester = resultSet.getString("semester")
        Record(id, Some(form), Some(course), startTime, Some(endTime), pause, alone, comment, semester)
      }
    }.toList
  }

}
