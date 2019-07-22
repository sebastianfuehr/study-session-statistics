package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VivePassionStatistics

import scala.io.StdIn.readLine

class UserController {

}
object UserController {

  def getUserInput: String = readLine("vpstats> ")
  def getUserInput(message: String): String = {
    println(message)
    getUserInput
  }

  /**
    * Class to represent a main command with following options or command specifications, separated by spaces.
    */
  case class ApplicationCommand(mainCommand: String, commandArray: Array[String])

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

  val PLEASE_TYPE_HELP = "Please type 'help' to get a list of available commands and options."
  def unknownUserInputNoOptions() = println(s"This command needs options. $PLEASE_TYPE_HELP")
  def unknownUserInputOption(notKnown: String) = println(s"Option '$notKnown' not known. $PLEASE_TYPE_HELP")
  def unknownUserInputCommand(notKnown: String) = println(s"Command '$notKnown' not known. $PLEASE_TYPE_HELP")
  def unknownUserInputCommandWithOptions(command: String): Unit = println(s"The command '$command' takes no options. $PLEASE_TYPE_HELP")

  // -------------------------------------------------------------------------------- methods
  /**
    * Prints the "help.txt" file on the console.
    */
  def printApplicationHelp: Unit = {
    val path: String = "resources/help.txt" // path of the "help.txt" file
    val bufferedSource = io.Source.fromFile(path)
    for (line <- bufferedSource.getLines) { // read each line of the csv file
      println(line)
    }
    bufferedSource.close
  }

  /**
    * Prints all options of the "vivepassionstats.properties" file on the console.
    */
  def printApplicationProperties: Unit = {
    val props = new Properties()
    props.load(new FileInputStream("resources/vivepassionstats.properties"))
    props.forEach((k, v) => println(k+":\t"+v))
  }

  /**
    * Validates the user input if the command continued with "new semester". If not error messages are printed.
    * If the command is correct the method calls {@link #RecordController.startNewSemester() startNewSemester}
    * and checks if the following table creation was successful. If not, prompts the user to retry or cancel
    * the operation.
    * @param arr String array which should consist of ["new", "semester"]
    */
  def createNewSemesterUserInputDialog(arr: Array[String]): Unit = {
    if (arr.length > 2 && arr(0).equals("new") && arr(1).equals("semester")) {
      if (arr.length > 2) unknownUserInputCommandWithOptions("start new semester")
      else {
        val success = RecordController.startNewSemster()
        if (!success) {
          val temp = getUserInput("The creation of a new table failed. Do you want to retry? [y/n]")
          if (temp.equals("y")) createNewSemesterUserInputDialog(arr)
        }
      }
    } else unknownUserInputCommand("start")
  }

  def addInputDialog(arr: Array[String]): Unit = {
    if (arr.length != 0) {
      arr(0) match {
        case "session" => RecordController.addNewLearnSessionDialog(arr.diff(arr(0) :: Nil))
        case notKnown => unknownUserInputOption(notKnown)
      }
    } else unknownUserInputCommand("add")
  }

  // -------------------------------------------------------------------------------- list
  /**
    * List the learn sessions in a specific way which gets specified by the method parameters.
    * @param options Optional parameters to manipulate the way the learn session get printed.
    */
  def listLearnSessions(options: Array[String]): Unit = {
    if (options.length == 0) {
      return
    }
    options.head match {
      case "--alone" =>
        VivePassionStatistics.learnSessions
          .filter(r => r.alone)
          .groupBy(r => r.getDate)
          .foreach(r => println(r.toString()))
      case "all" =>
        VivePassionStatistics.learnSessions
          .groupBy(r => r.getDate)
          .foreach(r => println(r.toString()))
      case "days" => RecordController.computeLearnDaysFromSession(VivePassionStatistics.learnSessions)// TODO implement printing of all study days
      case notKnown => unknownUserInputOption(notKnown)
    }
    if (options.length > 1) listLearnSessions(options.diff(options(0)::Nil))
  }

  // -------------------------------------------------------------------------------- analyse
  def makeAnalysis(options: Array[String]): Unit = {
    val list = VivePassionStatistics.learnSessions
    if (options.length != 0) {
      options(0) match {
        case "--alone" => println(s"Learn time alone: ${StatisticsController.getLearningTimeAlone(list, true)} h")
        case "--group" => println(s"Learn time in a group: ${StatisticsController.getLearningTimeAlone(list, false)} h")
        case "all" => StatisticsController.printAllStats()
        case notKnown => unknownUserInputOption(notKnown)
      }
      if (options.length > 1) makeAnalysis(options.diff(options(0) :: Nil))
    } else unknownUserInputNoOptions()
  }

}