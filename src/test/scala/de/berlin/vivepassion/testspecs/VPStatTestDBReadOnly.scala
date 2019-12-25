package de.berlin.vivepassion.testspecs

import java.time.LocalDateTime

import de.berlin.vivepassion.entities.{Record, RecordTests, Semester, StudyDay}
import org.scalatest.{BeforeAndAfterAll, Suites}
import VPStatTestConfig._
import de.berlin.vivepassion.io.database.DbRepositoryRecordTest

/**
 * Class which provides a local database with entries in all tables for
 * testing classes which read only from the database.
 *
 * @see RecordTests
 * @see DbRepositoryRecordTest
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class VPStatTestDBReadOnly extends Suites(new RecordTests, new DbRepositoryRecordTest) with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    val testRecord: Record = Record(0, Some("Doing homework"),
      Some("ProgC"),
      LocalDateTime.parse("2007-03-12T15:30:00"),
      Some(LocalDateTime.parse("2007-03-12T16:15:00")),
      7, alone = true, None, "SS19")

    dbTestController.createDatabase()
    dbTestController.clearAllTables()
    dbTestRepository.saveSemester(Semester("SS19"))
    dbTestRepository.saveStudyDay(StudyDay.makeStudyDay("12.03.2007"))
    dbTestRepository.saveStudyForm("Doing homework")
    dbTestRepository.saveCourse("ProgC")
    dbTestRepository.saveRecord(testRecord)
  }

  override def afterAll(): Unit = {
    //dbTestController.clearAllTables()
  }

}
