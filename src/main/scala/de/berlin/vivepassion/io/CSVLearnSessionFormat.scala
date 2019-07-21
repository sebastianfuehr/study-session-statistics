package de.berlin.vivepassion.io

case class CSVLearnSessionFormat(csvSeparator: Char, dateColumn: Int, startTimeColumn: Int, endTimeColumn: Int,
                                 pauseColumn: Int, remainingTimeColumn: Int, formColumn: Int, aloneColumn: Int,
                                 courseColumn: Int, aloneKeyWord: String) {

  /**
    * Default order of columns for a csv file with persisted records.
    * @param csvSeparator Character which separates the values of the csv.
    */
  def this(csvSeparator: Char) = {
    this(csvSeparator, 0, 2, 3, 4, 5, 6, 7, 8, "Allein")
  }

}
