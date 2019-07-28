package de.berlin.vivepassion.controller

import java.time.LocalDate

import de.berlin.vivepassion.VivePassionStatistics
import de.berlin.vivepassion.entities.Record

class StatisticsController {

}
object StatisticsController {

  /**
    * Prints all statistics which the StatisticsController can calculate.
    */
  def printAllStats(): Unit = {
    println("---------------------------------------------------------------------------------------------")
    val temp = VivePassionStatistics.learnSessions

    println("Complete learning time: "
      + BigDecimal(temp.foldLeft(0)((acc, r) => acc + r.getSessionLength.toInt) / 60.0)
        .setScale(2, BigDecimal.RoundingMode.HALF_UP)
      + " h\n")

    println(s"Learning time alone: ${StatisticsController.getLearningTimeAlone(temp, true)} h")
    println(s"Learning time in a group: ${StatisticsController.getLearningTimeAlone(temp, false)} h\n")

    println("Average learning time per day: "
      + getAverageLearningTime(temp)
      + " minutes/day\n")

    println("Learning time by course [in hours]:") // TODO implement or check
    val learningTimeByCourse = temp.groupBy(r => r.course)
    for (course <- learningTimeByCourse) {
      val courseName = if (course._1.equals("")) "Other" else course._1
      println(courseName + ":\t\t"
        + BigDecimal(course._2.foldLeft(0)((acc, r) => acc + r.getSessionLength.toInt) / 60.0)
          .setScale(2, BigDecimal.RoundingMode.HALF_UP)
        + " h")
    }

    println("---------------------------------------------------------------------------------------------")
  }

  /**
    * Calculates the average learning time per day.
    * @param list List of learn sessions (Record-Entity) to calculate the average from.
    * @return Double value which represents the average learning time per day.
    */
  def getAverageLearningTime(list: List[Record]): Double = {
    BigDecimal(list.groupBy(r => r.getDate)
      .foldLeft(0)(
        (acc, r) => acc + r._2.foldLeft(0)((acc, r2) => acc + r2.getSessionLength.toInt)
      ) / list.groupBy(r => r.getDate).size.toDouble).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  /**
    *
    * @param list List of learn sessions (Record-Entity).
    * @param alone True for total learning time alone, false for total learning time in a group.
    * @return Double value which represents either the total learning time alone or in a group.
    */
  def getLearningTimeAlone(list: List[Record], alone: Boolean): Double = {
    BigDecimal(list
      .filter(r => r.alone == alone)
      .map(r => r.getSessionLength)
      .sum / 60.0)
      .setScale(2, BigDecimal.RoundingMode.HALF_UP)
      .toDouble
  }

  /**
    * Filters for all sessions which where held on the specific date and sums them up.
    *
    * @param list List of learn sessions (Record-Entity)
    * @param date The date for which one wants the learning time.
    * @return
    */
  def getLearningTimeForDate(list: List[Record], date: LocalDate): Double = {
    BigDecimal(list
      .filter(r => r.getDate.equals(date))
      .map(r => r.getSessionLength)
      .sum / 60.0)
      .setScale(2, BigDecimal.RoundingMode.HALF_UP)
      .toDouble
  }

}