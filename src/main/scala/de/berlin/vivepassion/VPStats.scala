package de.berlin.vivepassion

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import de.berlin.vivepassion.VPSConfiguration.properties
import de.berlin.vivepassion.controller.StatisticsController
import de.berlin.vivepassion.entities.Record
import de.berlin.vivepassion.io.CSVFileLoader
import de.berlin.vivepassion.io.database.{DBController, DBRepository}
import scopt.OptionParser

/**
  * This object is the program entry point and terminal interface.
  */
object VPStats extends App {

  val dbController: DBController = new DBController(properties.getProperty("db_url"))
  val dbRepository: DBRepository = new DBRepository(dbController)

  dbController.createDatabase

  val testTablePath: String = "./src/main/resources/tables/Studiumsorganisation_Semester_3.csv"

  val parser = new OptionParser[Config]("vpstat") {
      head("Vivepassion Statistics", "0.0.1")

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
        .children(
          opt[String]('f', "form")
            .optional()
            .action((input, config) => config.copy(form = input))
            .text("specify the form of studying"),
          opt[String]('c', "course")
            .optional()
            .action((input, config) => config.copy(course = input))
            .text("university course"),
          opt[String]('b', "begin")
            .action((input, config) => config.copy(startTime = input))
            .text("override the begin time of the study session"),
          opt[String]('c', "comment")
            .optional()
            .action((input, config) => config.copy(comment = input))
            .text("optional comment for the study session"),
          opt[String]("studyForm")
            .optional()
            .action((input, config) => config.copy(semester = input))
            .text("the studyForm of the study session " +
              "(if not given, the last studyForm of the last registered studyForm is used)")
        )

      cmd("pause")
        .action((_, config) => config.copy(mode = "pause"))
        .text("pause the current study session")

      cmd("resume")
        .action((_, config) => config.copy(mode = "stop"))
        .text("resume the paused study session")

      cmd("stop")
        .action((_, config) => config.copy(mode = "stop"))
        .text("stop the current study session.\n " +
          "The application will ask for the form of learning and the course.")

  }

  /** This variable represents the status of the debug mode. If true additional error messages are printed. */
  val debugMode = parser
                .parse(args, Config()) // enabling/disabling debug mode
                .getOrElse(throw new Exception("Config file couldn't be read."))
                .debug
  if (debugMode) println("Debug mode active")

  val learnSessions = CSVFileLoader.getListOfCSVFile(testTablePath)

  parser.parse(args, Config()) match { // parse the user input

    case config@Some(Config(_, "analyze", _, _, _ , _, _, _, _, _, _, _)) =>    // analysing mode (default)
      config match {
        case Some(Config(_, _, aloneBoolean, _, _, _ , _, _, _, _, _, _)) =>
          println(s"Learn time ${if (aloneBoolean) "alone" else "in a group"}: " +
            s"${StatisticsController.getLearningTimeAlone(VPStats.learnSessions, aloneBoolean)} h")
      }


    case config@Some(Config(_, "list", _, _, _ , _, _, _, _, _, _, _)) =>       // list tables mode
      config match {
        // print all study sessions done alone // TODO implement method to print study sessions grouped by time interval and for alone or in a group
        case Some(Config(_, _, true, _, _ , _, _, _, _, _, _, _)) =>
          println("Study sessions alone:")
          VPStats.learnSessions
            .filter(r => r.alone)
            .sortBy(r => r.id)
            .foreach(r => println(r.toString()))
        // print all study sessions grouped by days
        case Some(Config(_, _, _, GroupByIntervals.Day, _ , _, _, _, _, _, _, _)) =>
          // TODO implement printing of all study days
        // print all study sessions
        case Some(Config(_, _, _, _, _ , _, _, _, _, _, _, _)) => VPStats.learnSessions
                                      .groupBy(r => r.getDate)
                                      .foreach(r => println(r.toString()))
      }


    case Some(Config(_, "start", alone, _, form, course, _, startTimeString, _, _, comment, semester)) =>
      dbRepository.semesterController.createSemesterIfNotExists(semester)
      dbRepository.studyFormController.createStudyFormIfNotExists(form)
      dbRepository.courseController.createCourseIfNotExists(course)
      val startTime: LocalDateTime = if (startTimeString == "") LocalDateTime.now()
      else LocalDateTime.parse(startTimeString, Record.dateTimeFormatter)
      val newRecord = Record(-1,
        form,
        course,
        startTime,
        null,
        0,
        alone,
        comment,
        semester)
      dbRepository.saveRecord(newRecord)
      println(s"Study session started at ${newRecord.getStartTimeString}")


    case Some(Config(_, "pause", _, _, _, _, _,_, _, _, _, _)) =>
      val tmpRec = dbRepository.getLastRecord()
      dbRepository.saveRecord(Record(tmpRec.id, tmpRec.form, tmpRec.course, tmpRec.startTime,
        LocalDateTime.now(), tmpRec.pause, tmpRec.alone, tmpRec.comment, tmpRec.semester))


    case Some(Config(_, "resume", _, _, _, _, _, _, _, _, _, _)) =>
      val tmpRec = dbRepository.getLastRecord()
      val pauseTime = tmpRec.endTime.until(LocalDateTime.now(), ChronoUnit.MILLIS).toInt
      dbRepository.saveRecord(Record(tmpRec.id, tmpRec.form, tmpRec.course, tmpRec.startTime,
        null, tmpRec.pause + pauseTime, tmpRec.alone, tmpRec.comment, tmpRec.semester))


    case Some(Config(_, "stop", _, _, _ , _, _, _, _, _, _, _)) =>
      val tmpRec = dbRepository.getLastRecord()
      val newRecord = Record(tmpRec.id, tmpRec.form, tmpRec.course, tmpRec.startTime,
        LocalDateTime.now(), tmpRec.pause, tmpRec.alone, tmpRec.comment, tmpRec.semester)
      dbRepository.saveRecord(newRecord)
      println(s"Record saved: $newRecord")


    case _ => println("Command not known.")
  }
}