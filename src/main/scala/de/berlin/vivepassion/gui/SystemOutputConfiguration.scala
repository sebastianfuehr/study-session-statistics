package de.berlin.vivepassion.gui

/**
 * Trait to specify methods to interact with the user. Can be used as an API for
 * GUI later on.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait SystemOutputConfiguration {

  /**
   * Print a message on a console, a canvas or something similar which has
   * to be specified in the implementation.
   * @param message Message to be printed.
   */
  def printMessageForUser(message: String): Unit

  /**
   * Print an error message on a console, a canvas or something similar which
   * has to be specified in the implementation.
   * @param message Message to be printed.
   */
  def printErrorMessage(message: String): Unit

}
