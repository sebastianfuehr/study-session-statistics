package de.berlin.vivepassion.controller

import de.berlin.vivepassion.entities.{Record, Semester}

/**
 * Trait to define functionality to save entities in the local database.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait EntityControllerInterface {

  /**
   * Save a course in the local database if it doesn't exist already.
   * @param courseName Course to be saved.
   * @return Boolean if the operation was successful.
   */
  def saveCourseIfNotExists(courseName: String): Boolean

  /**
   * Save a semester in the local database if it doesn't exist already.
   * @param semesterName Semester to be saved.
   * @return True if the semester didn't exist yet, false otherwise.
   */
  def saveSemesterIfNotExists(semesterName: String): Boolean

  /**
   * Save a semester in the local database if it doesn't exist already.
   * @param semester Semester to be saved.
   * @return True if the semester didn't exist yet, false otherwise.
   */
  def saveSemesterIfNotExists(semester: Semester): Boolean

  /**
   * Save a study form in the local database if it doesn't exist already.
   * @param studyFormName Form of study to be saved.
   * @return True if the form of study didn't exist yet, false otherwise.
   */
  def saveStudyFormIfNotExists(studyFormName: String): Boolean

  /**
   * Save a study day in the local database if it doesn't exist already.
   * @param studyDayString Study day to be saved.
   * @return True if the study day didn't exist yet, false otherwise.
   */
  def saveStudyDayIfNotExists(studyDayString: String): Boolean

  /**
   * Save a study session in the local database table STUDY_SESSION if it doesn't exist yet.
   * @param record Record to be saved.
   * @return True if the study session didn't exist yet, false otherwise.
   */
  def saveRecordIfNotExists(record: Record): Boolean

}
