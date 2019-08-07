package de.berlin.vivepassion.entities

import java.sql.ResultSet

/**
 * Abstract entity class.
 * @tparam T Type of the entity
 */
abstract class Entity[T] {
  /**
   * Converts a java ResultSet into a scala List[T].
   * @param resultSet ResultSet to convert
   * @return List of T
   */
  def fromResultSet(resultSet: ResultSet): List[T]
}
