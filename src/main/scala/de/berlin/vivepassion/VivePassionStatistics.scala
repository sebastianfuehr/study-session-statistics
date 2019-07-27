package de.berlin.vivepassion

import de.berlin.vivepassion.controller.{RecordController, StatisticsController, UserController}
import de.berlin.vivepassion.io.CSVFileLoader
import scopt.OParser

/**
  * This object is the program entry point and terminal interface.
  */
object VivePassionStatistics extends App {

  val learnSessions = CSVFileLoader.getListOfCSVFile
  var isInterrupted = false

  /** Load all properties and prepare application launch. */
  VPSConfiguration.init()

  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("vpstats"),
      head("vpstats", "0.1"),
      // scopt implemented options
      help('h', "help").text("prints this usage text"),
      version('v', "version"),
      // analysis options
      opt[Unit]('a', "alone")
        .text("total study time alone (default)"),
      opt[Unit]('g', "group")
        .action((_, config) => config.copy(alone = false))
        .text("total study time in a group"),
      // commands
      cmd("list")
        .action((_, config) => config.copy(mode = "list"))
        .text("list all learn sessions")
        .children(
          opt[Unit]('a', "alone")
            .action((_, config) => config.copy(alone = true))
            .text("list study sessions done alone")
        ),
      cmd("add")
        .action((_, config) => config.copy(mode = "add"))
        .text("add a new learn session"),
      cmd("options")
        .text("prints the application options")
        .children(
          opt[Unit]("set")
            .text("overwrites the specific option")
        )
    )
  }

  OParser.parse(parser, args, Config()) match {
    case config@Some(Config("analyse", _, _)) =>    // analysing mode (default)
      config match {
        case Some(Config(_, aloneBoolean, _)) =>
          println(s"Learn time ${if (aloneBoolean) "alone" else "in a group"}: " +
            s"${StatisticsController.getLearningTimeAlone(learnSessions, aloneBoolean)} h")
        case Some(Config(_, _, _)) => StatisticsController.printAllStats()
      }

    case config@Some(Config("list", _, _)) =>       // list tables mode
      config match {
        // print all study sessions done alone // TODO implement method to print study sessions grouped by time interval and for alone or in a group
        case Some(Config(_, true, _)) => VivePassionStatistics.learnSessions
                                        .filter(r => r.alone)
                                        .groupBy(r => r.getDate)
                                        .foreach(r => println(r.toString()))
        // print all study sessions grouped by days
        case Some(Config(_, _, GroupByIntervals.Day)) =>
          RecordController.computeLearnDaysFromSession(VivePassionStatistics.learnSessions)// TODO implement printing of all study days
        // print all study sessions
        case Some(Config(_, _, _)) => VivePassionStatistics.learnSessions
                                      .groupBy(r => r.getDate)
                                      .foreach(r => println(r.toString()))
      }


    case config@Some(Config("options", _, _)) =>    // options mode
      config match {
        case Some(Config(_, _, _)) => UserController.printApplicationProperties
      }


    case _ => println("Command not known.")
  }
}
