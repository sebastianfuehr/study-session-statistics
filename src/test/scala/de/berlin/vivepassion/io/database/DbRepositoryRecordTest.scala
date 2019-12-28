package de.berlin.vivepassion.io.database

import java.time.LocalDateTime

import de.berlin.vivepassion.testspecs.VPStatTestDB
import de.berlin.vivepassion.testspecs.VPStatTestConfig._

/**
 * A test class for testing the functionality of the class VPStatsDBRepository.
 * It provides specific tests in addition to DBRepositoryTest to verify different
 * cases of the manipulation of Record entities.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
class DbRepositoryRecordTest extends VPStatTestDB {

  "The DBRepository" should "retrieve the start time of a testRecord correctly" in {
    assert(dbTestRepository.getLastRecord.startTime === LocalDateTime.parse("2018-01-06T20:00"))
  }

  it should "retrieve the end time of a testRecord correctly" in {
    assert(dbTestRepository.getLastRecord.endTime === Some(LocalDateTime.parse("2018-01-06T21:30")))
  }

}
