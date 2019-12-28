package de.berlin.vivepassion.gui

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.entities.Semester

/**
 * Class which provides dialogues to interact with the user to support the
 * creation of new entities for the database.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class Dialogues(io: UserInteractionInstance) {

  /**
   * Dialogue which prompts the user to provide data for the creation of a
   * new Semester.
   * @param name Name of the new Semester
   * @return New Semester with the attributes specified by the user.
   */
  def createSemesterDialogue(name: String): Semester = {
    val startDateStr = io.readUserInputLine("Which date is the first day of the semester?")
    val endDateStr = io.readUserInputLine("Which is the last day?")

    val dateFormatStr: String = VPSConfiguration.properties.getProperty("default_date_format")
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatStr)
    val startDate: LocalDate = LocalDate.parse(startDateStr, dateFormatter)
    val endDate: LocalDate = LocalDate.parse(endDateStr, dateFormatter)

    Semester(-1, name, startDate, endDate)
  } // ----- End of createSemesterDialogue(name: String)

}
