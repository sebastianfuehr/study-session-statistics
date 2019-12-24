package de.berlin.vivepassion.io.database

import java.time.LocalDateTime

import de.berlin.vivepassion.entities.Record
import de.berlin.vivepassion.testspecs.VPStatSpec
import org.scalatest.BeforeAndAfterAll

class DbRepositoryRecordTest extends VPStatSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    dbTestController.clearAllTables()
    dbTestRepository.saveRecord(Record(0, None, None, LocalDateTime.parse("2007-03-12T16:45"),
      Some(LocalDateTime.parse("2007-03-12T17:10")), 7, alone = true, None, "SS19"))
  }


  "The DBRepository" should "retrieve the start time of a record correctly" in {
    assert(dbTestRepository.getRecords.head.startTime === LocalDateTime.parse("2007-03-12T16:45"))
  }

  it should "retrieve the end time of a record correctly" in {
    assert(dbTestRepository.getRecords.head.endTime === Some(LocalDateTime.parse("2007-03-12T17:10")))
  }


  override def afterAll(): Unit = dbTestController.clearAllTables()

}
