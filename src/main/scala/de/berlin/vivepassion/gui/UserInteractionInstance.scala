package de.berlin.vivepassion.gui

import scala.io.StdIn

/**
 * Class with specifies how the application interact with the user.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class UserInteractionInstance extends SystemOutputConfiguration with UserInputConfiguration {

  /**
   * @inheritdoc
   * @param message Message to be printed.
   */
  override def printMessageForUser(message: String): Unit = println(message)

  /**
   * @inheritdoc
   * @param message Message to be printed.
   */
  override def printErrorMessage(message: String): Unit = println(message)

  /**
   * @inheritdoc
   * @param message Message for the user.
   * @return User input as a string.
   */
  override def readUserInputLine(message: String): String = StdIn.readLine(message)

}
