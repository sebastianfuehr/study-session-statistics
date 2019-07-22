package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime}
import java.util.Properties

import de.berlin.vivepassion.entities.{LearnDay, Record}
import de.berlin.vivepassion.exceptions.MissingInputValueException

import scala.collection.mutable.ListBuffer

class RecordController {

}
object RecordController {

  val PROPERTIES_PATH = "resources/vivepassionstats.properties"

  var properties: Properties = new Properties()
  properties.load(new FileInputStream(PROPERTIES_PATH))

  def startNewSemster(): Boolean = { // TODO implement method
    val term: String = UserController.getUserInput("Which term is the new semester?")

    return false;
  }

  /**
    * Prompts the user for input in form of 'property=value' and tries to map these values to a new instance
    * of Record.
    * @param callParams String array of key-value-pairs to be cast into a new Record instance.
    */
  def addNewLearnSessionDialog(callParams: Array[String]): Unit = { // TODO implement method
    val params = callParams.map(e => {
      val entryTuple = e.split("=")
      (entryTuple(0), entryTuple(1))
    })

    // attributes of the new learn session
    var sessionStartTime: LocalDateTime = null
    var sessionEndTime: LocalDateTime = null
    var sessionForm: String = ""
    var sessionCourse: String = ""
    var sessionPause: Int = 0
    var sessionAlone: Boolean = true
    var sessionComment: String = ""
    var sessionPlanned: Int = 0

    if (!params.map(e => e._1).contains("date"))
      throw new MissingInputValueException(s"'date' must be defined. ${UserController.PLEASE_TYPE_HELP}")
    if (!params.map(e => e._1).contains("start"))
      throw new MissingInputValueException(s"'start' must be defined. ${UserController.PLEASE_TYPE_HELP}")

    val date_time_format: String = properties.getProperty("default_date_format")
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(date_time_format)
    val dateTuple = params.filter(e => e._1.equals("date")).head
    val sessionDate: LocalDate = LocalDate.parse(dateTuple._2, dateTimeFormatter)

    for (entry <- params diff ("date", sessionDate.toString)::Nil) {
      entry match {
        case ("start", startTime) => sessionStartTime = LocalDateTime.parse(sessionDate + "T" + startTime + ":00")
        case ("end", endTime) =>sessionEndTime = LocalDateTime.parse(sessionDate + "T" + endTime + ":00")
        case ("form", form) => sessionForm = form
        case ("course", course) => sessionCourse = course
        case ("pause", pause) => sessionPause = Integer.parseInt(pause)
        case ("alone", alone) => sessionAlone = alone.toBoolean
        case ("comment", comment) => sessionComment = comment
        case ("planned", planned) => sessionPlanned = planned.toInt
        case ("date", _) => //do nothing
        case (keyString, _) => UserController.unknownUserInputOption(keyString)
      }
    }
    Record(
      sessionForm,
      sessionCourse,
      sessionStartTime,
      sessionEndTime,
      sessionPause,
      sessionAlone,
      sessionComment
    )
    // the new session gets written into the file of the active semester
  }

  /**
    *
    * @param list
    */
  def computeLearnDaysFromSession(list: List[Record]) = {
    val groupedList = list.groupBy(r => r.getDate)
    val listBuffer: ListBuffer[LearnDay] = ListBuffer.empty
    for (tuple <- groupedList) {
      val studyTime = tuple._2.map(r => r.getSessionLength).sum.toInt
      listBuffer += LearnDay(tuple._1, studyTime, 0, tuple._2)
    }
  }
}
