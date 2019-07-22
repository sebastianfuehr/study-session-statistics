package de.berlin.vivepassion.entities

import java.time.LocalDate

case class LearnDay(date: LocalDate, studyTime: Int, plannedStudyTime: Int, studySessions: List[Record]) {

  /**
    * @return Study time of all study sessions of the day.
    */
  def getCompleteStudyTime: Double =
    BigDecimal(studySessions.map(r => r.getSessionLength).sum / 60)
      .setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

}
object LearnDay {

}