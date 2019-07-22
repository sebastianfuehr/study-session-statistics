package de.berlin.vivepassion

import de.berlin.vivepassion.controller.UserController
import de.berlin.vivepassion.io.CSVFileLoader

object VivePassionStatistics extends App {

  val learnSessions = CSVFileLoader.getListOfCSVFile
  var isInterrupted = false

  /** Load all properties and prepare application launch. */
  VPSConfiguration.init()

  // program circle
  println("VivePassion Statistics started")
  while(!isInterrupted) {
    UserController.computeUserAction
  }
  println("VivePassion Statistics stopped")

}
