package de.berlin.vivepassion.entities

import java.sql.ResultSet

/**
 * Entity class which represents the form of studying.
 *
 * @param id Id of the study form in the database.
 * @param name Name of the study form (e.g. 'doing homework' or 'memorize flashcards').
 */
case class StudyForm(id: Long, name: String) extends Entity[StudyForm] {

  /**
   * @inheritdoc
   * @return Saved instance of T.
   */
  @Override
  override def getEntityClass: StudyForm = this

}
object StudyForm extends EntityObjectInterface[StudyForm] {

  /**
   * Converts a java ResultSet into a scala List[StudyForm].
   * @param resultSet ResultSet to convert
   * @return List of study forms
   */
  @Override
  override def resultSetToList(resultSet: ResultSet): List[StudyForm] = {
    new Iterator[StudyForm] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext = resultSet.next()
      def next: StudyForm = { // here a typecast happens
        val name = resultSet.getString("FORM_NAME")
        val id = resultSet.getLong("ID")
        StudyForm(id, name)
      }
    }.toList
  }

}