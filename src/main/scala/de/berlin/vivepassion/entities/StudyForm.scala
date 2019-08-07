package de.berlin.vivepassion.entities

import java.sql.ResultSet

/**
 * Entity class which represents the form of studying.
 *
 * @param id Id of the study form in the database.
 * @param name Name of the study form (e.g. 'doing homework' or 'memorize flashcards').
 */
case class StudyForm(id: Long, name: String) {}
object StudyForm extends Entity[StudyForm] {

  /**
   * Converts a java ResultSet into a scala List[StudyForm].
   * @param resultSet ResultSet to convert
   * @return List of study forms
   */
  override def fromResultSet(resultSet: ResultSet): List[StudyForm] = {
    new Iterator[StudyForm] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext = resultSet.next()
      def next() = { // here a typecast happens
        val name = resultSet.getString("form_name")
        val id = resultSet.getLong("id")
        StudyForm(id, name)
      }
    }.toList
  }

}