package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

object UserController {

  /*
  def computeUserAction: Unit = {
    val inputTemp: Array[String] = getUserInput.split(" ")
    val input = ApplicationCommand(inputTemp.head, inputTemp.diff(inputTemp.head::Nil))
    input match {
      case ApplicationCommand("exit", _) => VivePassionStatistics.isInterrupted = true // exit the application
      case ApplicationCommand("help", _) => printApplicationHelp
      case ApplicationCommand("options", _) => printApplicationProperties // list the content of the "vivepassionstats.properties"
      case ApplicationCommand("start", arr) => createNewSemesterUserInputDialog(arr)
      case ApplicationCommand("session", arr) => //TODO implement method to start measuring the time
      case ApplicationCommand("add", arr) => addInputDialog(arr)
      case ApplicationCommand("list", arr) => listLearnSessions(arr)
      case ApplicationCommand("analyse", arr) => makeAnalysis(arr) // analyse a specific parameter
      case ApplicationCommand(notKnownCommand, _) => unknownUserInputCommand(notKnownCommand)
    }
  }
   */

  val PLEASE_TYPE_HELP = "Please type 'help' to get a list of available commands and options."
  def unknownUserInputNoOptions() = println(s"This command needs options. $PLEASE_TYPE_HELP")
  def unknownUserInputOption(notKnown: String) = println(s"Option '$notKnown' not known. $PLEASE_TYPE_HELP")
  def unknownUserInputCommand(notKnown: String) = println(s"Command '$notKnown' not known. $PLEASE_TYPE_HELP")
  def unknownUserInputCommandWithOptions(command: String): Unit = println(s"The command '$command' takes no options. $PLEASE_TYPE_HELP")

  // -------------------------------------------------------------------------------- methods
  /**
    * Prints all options of the "vivepassionstats.properties" file on the console.
    */
  def printApplicationProperties: Unit = {
    val props = new Properties()
    props.load(new FileInputStream("resources/vivepassionstats.properties"))
    props.forEach((k, v) => println(k+":\t"+v))
  }

}