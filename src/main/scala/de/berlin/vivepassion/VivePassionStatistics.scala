package de.berlin.vivepassion

import de.berlin.vivepassion.controller.{RecordController, StatisticsController}
import de.berlin.vivepassion.io.{CSVFileLoader, SQLiteJDBCDriverConnection}
import scopt.OptionParser

/**
  * This object is the program entry point and terminal interface.
  */
object VivePassionStatistics extends App {

  val learnSessions = CSVFileLoader.getListOfCSVFile("./src/main/resources/tables/Studiumsorganisation_Semester_3.csv")

  var debugMode = false
  var isInterrupted = false

  val parser = new OptionParser[Config]("VivePassion Statistics") {
      head("vpstats", "0.1")

      // scopt implemented options
      help("help").text("prints this usage text")

      version("version")

      note("\nThe program is in analysing mode by default. This means that you'll get\n" +
        " analysed output if a command is invoked. For other functionality please type\n" +
        " the corresponding command.")

      // analysis options
      note("\nList of options in analysing mode:\n")

      opt[Unit]('a', "alone")
        .text("total study time alone (default)")

      opt[Unit]('g', "group")
        .action((_, config) => config.copy(alone = false))
        .text("total study time in a group")

      opt[Unit]("debug")
          .action((_, config) => config.copy(debug = true))
          .text("activate debug messages")

      // commands
      note("\nAvailable commands plus options:\n")

      cmd("list")
        .action((_, config) => config.copy(mode = "list"))
        .text("list all learn sessions")
        .children(
          opt[Unit]('a', "alone")
            .action((_, config) => config.copy(alone = true))
            .text("list study sessions done alone"),
          opt[Unit]('g', "group")
            .action((_, config) => config.copy(alone = false))
            .text("list study sessions done in a group")
        )

      cmd("start")
        .action((_, config) => config.copy(mode = "start"))
        .text("start a new study session")

      cmd("stop")
        .action((_, config) => config.copy(mode = "stop"))
        .text("stop the current study session.\n " +
          "The application will ask for the form of learning and the course.")

    /* TODO Does the application really need this functionality?
      cmd("add")
        .action((_, config) => config.copy(mode = "add"))
        .text("add a new learn session")
        .children(
          arg[String]("form")
            .text("what has been done during the study session"),
          arg[String]("course")
            .text("which course has been studied"),
        )
     */

  }

  debugMode = parser.parse(args, Config()).get.debug // enabling debug mode

  /** Load all properties and prepare application launch. */
  VPSConfiguration.init()

  SQLiteJDBCDriverConnection.connect()

  parser.parse(args, Config()) match {
    // parse command
    case config@Some(Config(_, "analyse", _, _, _ , _, _, _, _, _)) =>    // analysing mode (default)
      config match {
        case Some(Config(_, _, aloneBoolean, _, _, _ , _, _, _, _)) =>
          println(s"Learn time ${if (aloneBoolean) "alone" else "in a group"}: " +
            s"${StatisticsController.getLearningTimeAlone(learnSessions, aloneBoolean)} h")
        case Some(Config(_, _, _, _, _ , _, _, _, _, _)) => StatisticsController.printAllStats()
      }


    case config@Some(Config(_, "list", _, _, _ , _, _, _, _, _)) =>       // list tables mode
      config match {
        // print all study sessions done alone // TODO implement method to print study sessions grouped by time interval and for alone or in a group
        case Some(Config(_, _, true, _, _ , _, _, _, _, _)) =>
          println("Study sessions alone:")
          VivePassionStatistics.learnSessions
            .filter(r => r.alone)
            .sortBy(r => r.id)
            .foreach(r => println(r.toString()))
        // print all study sessions grouped by days
        case Some(Config(_, _, _, GroupByIntervals.Day, _ , _, _, _, _, _)) =>
          RecordController.computeLearnDaysFromSession(VivePassionStatistics.learnSessions)// TODO implement printing of all study days
        // print all study sessions
        case Some(Config(_, _, _, _, _ , _, _, _, _, _)) => VivePassionStatistics.learnSessions
                                      .groupBy(r => r.getDate)
                                      .foreach(r => println(r.toString()))
      }


    case Some(Config(_, "start", _, _, _ , _, _, _, _, _)) =>


    case Some(Config(_, "stop", _, _, _ , _, _, _, _, _)) =>


    case _ => println("Command not known.")
  }
}
