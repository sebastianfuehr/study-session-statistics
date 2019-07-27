package de.berlin.vivepassion.io

import de.berlin.vivepassion.entities.Record

class CSVFileLoader {

}
object CSVFileLoader {

  val path: String = "resources/tables/Studiumsorganisation_Semester_3.csv"

  /**
    * Reads all lines of the csv file except the first line which is supposed to hold the column headings.
    * @return List of learn sessions in form of the Record entity.
    */
  def getListOfCSVFile: List[Record] = {
    val bufferedSource = io.Source.fromFile(path)
    var resultList: List[Record] = List.empty
    var index = 0
    for (line <- bufferedSource.getLines) { // read each line of the csv file
      if (index != 0) resultList +:= Record.fromLine(line, index)
      index += 1
    }
    bufferedSource.close
    resultList
  }

}
