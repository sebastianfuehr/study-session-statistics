package de.berlin.vivepassion.io

import de.berlin.vivepassion.VPStats
import de.berlin.vivepassion.controller.EntityControllerInterface
import de.berlin.vivepassion.entities.{Record, Semester}

import scala.collection.mutable.ListBuffer

/**
 * Controller object to read the contents of a csv file and parse them to study sessions.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
object CSVFileLoader extends CSVFileLoaderInterface {

  @Override
  override def getListOfCSVFile(path: String): List[Record] = {
    val bufferedSource = io.Source.fromFile(path)
    val resultList: ListBuffer[Record] = ListBuffer.empty
    var index: Long = 0 // represents the id of the record
    for (line <- bufferedSource.getLines) { // read each line of the csv file
      if (index != 0) resultList += Record.fromLine(line, index)
      index += 1
    }
    bufferedSource.close
    resultList.toList
  } // ----- End of getListOfCSVFile

  /**
   * @inheritdoc
   *
   * @param csvPath Path of the csv file to be imported.
   * @param entityController TODO
   * @return Boolean if the operation was successful.
   *
   * @see EntityControllerInterface#saveSemesterIfNotExists(semester: Semester)
   * @see EntityControllerInterface#saveCourseIfNotExists(courseName: String)
   * @see EntityControllerInterface#saveStudyFormIfNotExists(studyFormName: String)
   * @see EntityControllerInterface#saveRecordIfNotExists(record: Record)
   */
  @Override
  override def importCsvIntoDatabase(csvPath: String, entityController: EntityControllerInterface): Boolean = {
    val list: List[Record] = CSVFileLoader.getListOfCSVFile(csvPath)

    if (VPStats.debugMode) println(s"Importing ${list.length} elements")

    val firstDay = list.map(rec => rec.getDate)
      .reduce((acc, elem) => if (elem.isBefore(acc)) elem else acc)
    val lastDay = list.map(rec => rec.getDate)
      .reduce((acc, elem) => if (elem.isAfter(acc)) elem else acc)
    val semesterName = list.map(rec => rec.getDate)
      .reduce((acc, elem) => if (elem.isBefore(acc)) elem else acc).toString //TODO test if semesterName name is right
    entityController.saveSemesterIfNotExists(Semester(-1, semesterName, firstDay, lastDay))

    for (element <- list) {
      element.course match {
        case Some(course) => entityController.saveCourseIfNotExists(course)
        case None         => // do nothing
      }
      element.form match {
        case Some(form) => entityController.saveStudyFormIfNotExists(form)
        case None       => // do nothing
      }
      entityController.saveRecordIfNotExists(element)
    }
    true
  } // ----- End of importCsvIntoDatabase

}
