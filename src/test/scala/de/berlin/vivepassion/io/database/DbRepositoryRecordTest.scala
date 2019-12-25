package de.berlin.vivepassion.io.database

import java.time.LocalDateTime

import de.berlin.vivepassion.testspecs.VPStatTestConfig._
import org.scalatest.{DoNotDiscover, FlatSpec}

/**
 * A test class for testing the functionality of the class VPStatsDBRepository.
 * It provides specific tests in addition to DBRepositoryTest to verify different
 * cases of the manipulation of Record entities.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
@DoNotDiscover
class DbRepositoryRecordTest extends FlatSpec {

  "The DBRepository" should "retrieve the start time of a record correctly" in {
    assert(dbTestRepository.getRecords.head.startTime === LocalDateTime.parse("2007-03-12T15:30"))
  }

  it should "retrieve the end time of a record correctly" in {
    assert(dbTestRepository.getRecords.head.endTime === Some(LocalDateTime.parse("2007-03-12T16:15")))
  }

}
