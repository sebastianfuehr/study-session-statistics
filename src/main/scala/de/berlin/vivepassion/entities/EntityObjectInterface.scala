package de.berlin.vivepassion.entities

import java.sql.ResultSet

/**
 * Trait to provide basic entity manipulating functionality for entity objects.
 *
 * @tparam T Type of the entity
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait EntityObjectInterface[T] {

  /**
   * Converts a java ResultSet into a scala List[T].
   * @param resultSet ResultSet to convert
   * @return List of type T
   */
  def resultSetToList(resultSet: ResultSet): List[T]

}
