package de.berlin.vivepassion.io

import de.berlin.vivepassion.VPSConfiguration

/**
 * Case class which holds information about the indices of columns of a csv file.
 *
 * @param csvSeparator Char which separates the different fields in the csv files. Typically ';'
 * @param dateColumn Index of the column which holds the dates of the study session.
 * @param startTimeColumn Column index of the start time.
 * @param endTimeColumn Column index of the end time.
 * @param pauseColumn Column index of the pause.
 * @param formColumn Column index of the persisted study forms.
 * @param aloneColumn Column index of the alone key word.
 * @param courseColumn Column index of the persisted courses.
 * @param commentColumn Column index of comments.
 * @param aloneKeyWord String which represents the status of working alone during a study session. E.g. "alone"
 *
 * @author Sebastian Führ
 * @version 0.1
 */
case class CSVLearnSessionFormat(csvSeparator: Char, dateColumn: Int, startTimeColumn: Int, endTimeColumn: Int,
                                 pauseColumn: Int, formColumn: Int, aloneColumn: Int, courseColumn: Int,
                                 commentColumn: Int, aloneKeyWord: String) {

  /**
    * Default constructor with a default order of columns for a csv file with persisted records.
    * @param csvSeparator Character which separates the values of the csv.
    */
  def this(csvSeparator: Char) = {
    this(csvSeparator, 0, 1, 2, 3, 4, 5, 6, 7, VPSConfiguration.langProps.getProperty("alone"))
  }

}
