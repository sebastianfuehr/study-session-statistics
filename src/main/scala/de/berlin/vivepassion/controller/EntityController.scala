package de.berlin.vivepassion.controller
import de.berlin.vivepassion.entities.{Record, Semester, StudyDay}
import de.berlin.vivepassion.gui.Dialogues
import de.berlin.vivepassion.io.database.VPStatsDBRepository

/**
 * Class which implements functionality to save entities in the local database.
 *
 * @see io.database.VPStatsDBRepository
 * @see gui.Dialogues
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class EntityController(dbRepository: VPStatsDBRepository) extends EntityControllerInterface {

  /**
   * @inheritdoc
   *
   * @param courseName The name of the course to be saved.
   * @return True if the course didn't exist yet, false otherwise.
   * @see VPStatsDBRepository#saveCourse
   */
  @Override
  override def saveCourseIfNotExists(courseName: String): Boolean = {
    if (courseName != null && !dbRepository.getCourses.contains(courseName)) {
      dbRepository.saveCourse(courseName)
      true
    } else false
  } // ----- End of saveCourseIfNotExists

  /**
   * @inheritdoc
   *
   * @param studyFormName The name of the study form to be saved.
   * @return True if the form of study didn't exist yet, false otherwise.
   * @see VPStatsDBRepository#saveStudyForm
   */
  @Override
  override def saveStudyFormIfNotExists(studyFormName: String): Boolean = {
    if (studyFormName != null && !dbRepository.getStudyForms.contains(studyFormName)) {
      dbRepository.saveStudyForm(studyFormName)
      true
    } else false
  } // ----- End of saveStudyFormIfNotExists

  /**
   * @inheritdoc
   *
   * @param semesterName The name of the semesterName to be saved.
   * @return True if the semester didn't exist yet, false otherwise.
   * @see VPStatsDBRepository#saveSemester
   * @see Dialogues#createSemesterDialogue
   */
  @Override
  override def saveSemesterIfNotExists(semesterName: String): Boolean = {
    if (semesterName != null && !dbRepository.getSemesters.contains(semesterName)) {
      println(s"The semester '$semesterName' was not found. A new one has to be created.")
      dbRepository.saveSemester(Dialogues.createSemesterDialogue(semesterName))
      true
    } else false
  } // ----- End of saveSemesterIfNotExists

  /**
   * @inheritdoc
   *
   * @param semester Semester to be saved.
   * @return True if the semester didn't exist yet, false otherwise.
   * @see VPStatsDBRepository#saveSemester
   */
  @Override
  override def saveSemesterIfNotExists(semester: Semester): Boolean = {
    if (semester.name != null && !dbRepository.getSemesters.contains(semester.name)) {
      dbRepository.saveSemester(semester)
      true
    } else false
  } // ----- End of saveSemesterIfNotExists

  /**
   * @inheritdoc
   *
   * @param studyDayString Study day to be saved.
   * @return True if the study day didn't exist yet, false otherwise.
   * @see StudyDay#makeStudyDay(studyDayString)
   */
  @Override
  override def saveStudyDayIfNotExists(studyDayString: String): Boolean = {
    if (studyDayString != null && !dbRepository.getStudyDaysAsStrings.contains(studyDayString)) {
      dbRepository.saveStudyDay(StudyDay.makeStudyDay(studyDayString))
      true
    } else false
  }

  /**
   * @inheritdoc
   *
   * @param record Record to be saved.
   * @return True if the study session didn't exist yet, false otherwise.
   * @see VPStatsDBRepository#saveRecord(record: Record)
   */
  @Override
  override def saveRecordIfNotExists(record: Record): Boolean = {
    if (record != null && !dbRepository.getRecords.contains(record)) {
      dbRepository.saveRecord(record)
      true
    } else false
  } // ----- End of saveRecordIfNotExists

}
