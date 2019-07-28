package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration

object RecordController {

  var properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.PROPERTIES_PATH))

  /*
  def computeLearnDaysFromSession(list: List[Record]) = { // TODO Am I important?
    val groupedList = list.groupBy(r => r.getDate)
    val listBuffer: ListBuffer[StudyDay] = ListBuffer.empty
    for (tuple <- groupedList) {
      val studyTime = tuple._2.map(r => r.getSessionLength).sum.toInt
      listBuffer += StudyDay(tuple._1, studyTime, 0, tuple._2)
    }
  }
   */
}
