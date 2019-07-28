package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.entities.{LearnDay, Record}

import scala.collection.mutable.ListBuffer

object RecordController {

  var properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.PROPERTIES_PATH))

  def saveRecord(record: Record): Unit = {
    val sql: String = "INSERT INTO "
  }

   /**
    * @param list
    */
  def computeLearnDaysFromSession(list: List[Record]) = { // TODO Am I important?
    val groupedList = list.groupBy(r => r.getDate)
    val listBuffer: ListBuffer[LearnDay] = ListBuffer.empty
    for (tuple <- groupedList) {
      val studyTime = tuple._2.map(r => r.getSessionLength).sum.toInt
      listBuffer += LearnDay(tuple._1, studyTime, 0, tuple._2)
    }
  }
}
