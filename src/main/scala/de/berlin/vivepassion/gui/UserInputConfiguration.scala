package de.berlin.vivepassion.gui

/**
 * Trait to specify methods to interact with the user. Can be used as an API for
 * GUI later on.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait UserInputConfiguration {

  /**
   * Read the user input of a user and return it as a string.
   * @param message Message for the user.
   * @return User input as a string.
   */
  def readUserInputLine(message: String): String

}
