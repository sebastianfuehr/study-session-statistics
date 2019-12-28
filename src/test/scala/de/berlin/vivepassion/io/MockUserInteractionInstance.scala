package de.berlin.vivepassion.io

import de.berlin.vivepassion.gui.UserInteractionInstance

/**
 * Test class which returns hard coded attributes to mock the input of a user.
 * TODO Search for a better solution to mock user input
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class MockUserInteractionInstance extends UserInteractionInstance {

  override def readUserInputLine(message: String): String = {
    message match {
      case "Which date is the first day of the semester?" => "01.01.2019"
      case "Which is the last day?" => "24.06.2019"
    }
  }

}
