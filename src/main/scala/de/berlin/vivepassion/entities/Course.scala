package de.berlin.vivepassion.entities

import java.sql.ResultSet

/**
 * Entity which represents an university course.
 *
 * @param id Id of the course in the database.
 * @param name Name of the course.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
case class Course(id: Long, name: String) extends Entity[Course] { //TODO add ects points, description, professor

  /**
   * @inheritdoc
   * @return Saved instance of T.
   */
  @Override
  override def getEntityClass: Course = this

}
object Course extends EntityObjectInterface[Course] {

  /**
   * Converts a java ResultSet into a scala List[Course].
   * @param resultSet ResultSet to convert
   * @return List of courses
   */
  @Override
  override def resultSetToList(resultSet: ResultSet): List[Course] = {
    new Iterator[Course] { // https://stackoverflow.com/questions/9636545/treating-an-sql-resultset-like-a-scala-stream
      def hasNext: Boolean = resultSet.next()
      def next(): Course = { // here a typecast happens
        val name = resultSet.getString("course_name")
        val id = resultSet.getLong("id")
        Course(id, name)
      }
    }.toList
  } // ----- End of resultSetToList
}