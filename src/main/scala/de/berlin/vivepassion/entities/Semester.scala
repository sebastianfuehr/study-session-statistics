package de.berlin.vivepassion.entities

import java.time.LocalDate

/**
 * Entity which represents a university semester with a variable lengths.
 *
 * @param id Id of the semester in the database.
 * @param name Name of the semester.
 * @param start Date of the first day of the semester.
 * @param end Date of the last day of the semester.
 */
case class Semester(id: Int, name: String, start: LocalDate, end: LocalDate) {

}
