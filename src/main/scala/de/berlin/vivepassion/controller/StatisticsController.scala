package de.berlin.vivepassion.controller

import java.time.LocalDate

import de.berlin.vivepassion.io.database.{VPStatsDBRepository}

class StatisticsController(dbRepository: VPStatsDBRepository) {

  /**
    * Prints all statistics which the StatisticsController can calculate.
    */
  def printAllStats(): Unit = {
    println("---------------------------------------------------------------------------------------------")
    val temp = dbRepository.getRecords

    println("Complete learning time: "
      + BigDecimal(temp.foldLeft(0)((acc, r) => acc + r.getSessionLength.toInt) / 60.0)
        .setScale(2, BigDecimal.RoundingMode.HALF_UP)
      + " h\n")

    println(s"Learning time alone: ${getLearningTimeAlone(true)} h")
    println(s"Learning time in a group: ${getLearningTimeAlone(false)} h\n")

    println("Average learning time per day: "
      + getAverageLearningTime()
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
    * Calculates the average learning time per day in minutes.
    * @return Double value which represents the average learning time per day.
    */
  def getAverageLearningTime(): Double = {
    val list = dbRepository.getRecords
    val result = list.groupBy(r => r.getDate)
      .foldLeft(0)(
        (acc, r) => acc + r._2.foldLeft(0)((acc, r2) => acc + r2.getSessionLength)
      ).toDouble
    val amtDays = list.groupBy(r => r.getDate).size
    result / amtDays
  }

  /**
   * Calculates the average learning time per day in minutes done alone or in a group.
   * @param alone Average learning time alone or in a group.
   * @return Double value which represents the average learning time per day done alone or in a group.
   */
  def getAverageLearningTimeAlone(alone: Boolean): Double = {
    val result = getLearningTimeAlone(alone) * 60
    val amtDays = dbRepository.getRecords.filter(r => r.alone).groupBy(r => r.getDate).size
    result / amtDays
  }

  /**
    *
    * @param alone True for total learning time alone, false for total learning time in a group.
    * @return Double value which represents either the total learning time alone or in a group.
    */
  def getLearningTimeAlone(alone: Boolean): Double = {
    val list = dbRepository.getRecords
    val result = list
      .filter(r => r.alone == alone)
      .map(r => r.getSessionLength)
      .sum.toDouble
    result / 60
  }

  /**
    * Filters for all sessions which where held on the specific date and sums them up.
    *
    * @param date The date for which one wants the learning time.
    * @return
    */
  def getLearningTimeForDate(date: LocalDate): Double = {
    val list = dbRepository.getRecords
    val result = list
      .filter(r => r.getDate.equals(date))
      .map(r => r.getSessionLength)
      .sum
    BigDecimal(result / 60).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

}