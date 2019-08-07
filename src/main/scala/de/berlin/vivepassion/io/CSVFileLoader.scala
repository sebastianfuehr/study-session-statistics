package de.berlin.vivepassion.io

import de.berlin.vivepassion.entities.Record

import scala.collection.mutable.ListBuffer

object CSVFileLoader {

  /**
    * Reads all lines of the csv file except the first line which is supposed to hold the column headings.
    * @return List of learn sessions in form of the Record entity.
    */
  def getListOfCSVFile(path: String): List[Record] = {
    val bufferedSource = io.Source.fromFile(path)
    val resultList: ListBuffer[Record] = ListBuffer.empty
    var index: Long = 0 // represents the id of the record
    for (line <- bufferedSource.getLines) { // read each line of the csv file
      if (index != 0) resultList += Record.fromLine(line, index)
      index += 1
    }
    bufferedSource.close
    resultList.toList
  }

}
