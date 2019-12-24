package de.berlin.vivepassion.entities

/**
 * Trait to provide basic entity manipulating functionality for entity classes.
 *
 * It is recommended that the entity provides getter methods
 * for all relevant attributes.
 *
 * @tparam T Type of the entity
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait Entity[T] {

  /**
   * Return the instance of the entity.
   * @return Saved instance of T.
   */
  def getEntityClass: T

}
