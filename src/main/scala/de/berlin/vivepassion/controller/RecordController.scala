package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.entities.{LearnDay, Record}

import scala.collection.mutable.ListBuffer

object RecordController {

  var properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.PROPERTIES_PATH))

  /**
    * Prompts the user for input in form of 'property=value' and tries to map these values to a new instance
    * of Record.
    * @param callParams String array of key-value-pairs to be cast into a new Record instance.
    */
  def addNewLearnSessionDialog(callParams: Array[String]): Unit = { // TODO implement method

  }

  /**
    * TODO Am I important?
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
