package de.berlin.vivepassion.entities

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.testspecs.VPStatTestConfig._
import org.scalatest.{BeforeAndAfterAll, DoNotDiscover, FlatSpec}

/**
 * A test class for testing the functionality of the entity class Record.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
@DoNotDiscover
class RecordTests extends FlatSpec with BeforeAndAfterAll {

  val testRecord: Record = Record(0, Some("Doing homework"),
                        Some("Prog2"),
                        LocalDateTime.parse("2007-03-12T15:30:00"),
                        Some(LocalDateTime.parse("2007-03-12T16:15:00")),
                        7, alone = true, None, "SS19")

  override def beforeAll(): Unit = {
    dbTestController.createDatabase()
    dbTestController.clearAllTables()
    dbTestRepository.saveSemester(Semester("SS19"))
    dbTestRepository.saveStudyDay(StudyDay.makeStudyDay("12.03.2007"))
    dbTestRepository.saveStudyForm("Doing homework")
    dbTestRepository.saveCourse("ProgC")
    dbTestRepository.saveRecord(testRecord)
  }



  "A testRecord companion object" should "generate a testRecord instance from a string" in {
    val temp = Record.fromLine("12.03.2007,15:30,16:15,7,Doing homework,alone,Prog2,", 0)
    assert(testRecord == temp)
  }

  ignore should "not generate a testRecord instance from a false string" in {

  }

  it should "convert a sql result set into a List[Record]" in {
    val tmpList: List[Record] = Record.resultSetToList(dbTestRepository.queryTableFor("STUDY_SESSION", "*"))
    assert(tmpList.length == 1)
  }

  it should "convert an 'isAlone' key word into a boolean" in {
    assert (
      Record.getIsAloneBoolean("alone", "alone")
        && !Record.getIsAloneBoolean("group", "alone")
    )
  }



  "A testRecord" should "return a date as a string in the right format" in {
    assert(testRecord.getDate == LocalDate.parse("2007-03-12"))
  }

  it should "calculate the right session lengths" in {
    val rec = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T16:00"),
                Some(LocalDateTime.parse("2007-03-12T16:45")), 0, alone = true, None, "SS19")
    assert(rec.getSessionLength === 45)
  }

  it should "calculate the right session lengths with pause" in {
    val rec = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T16:00"),
      Some(LocalDateTime.parse("2007-03-12T16:45")), 15, alone = true, None, "SS19")
    assert(rec.getSessionLength === 30)
  }

  it should "calculate the right session lengths over an hour gap" in {
    val rec = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T16:45"),
      Some(LocalDateTime.parse("2007-03-12T17:10")), 0, alone = true, None, "SS19")
    assert(rec.getSessionLength === 25)
  }

  it should "calculate the right session lengths over an hour gap with pause" in {
    val rec = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T16:45"),
      Some(LocalDateTime.parse("2007-03-12T17:10")), 7, alone = true, None, "SS19")
    assert(rec.getSessionLength === 18)
  }

  it should "calculate the right session lengths over a day gap" in {
    val rec = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T23:30"),
      Some(LocalDateTime.parse("2007-03-13T00:45")), 0, alone = true, None, "SS19")
    assert(rec.getSessionLength === 75)
  }

  it should "calculate the right session lengths over a day gap with pause" in {
    val rec = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T23:30"),
      Some(LocalDateTime.parse("2007-03-13T00:45")), 14, alone = true, None, "SS19")
    assert(rec.getSessionLength === 61)
  }

  it should "throw an exception if the end time would be before the start time of the session" in {
    val thrown = intercept[Exception] {
      Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T16:00"),
        Some(LocalDateTime.parse("2007-03-12T15:45")), 0, alone = true, None, "SS19")
    }
    assert(thrown.isInstanceOf[IllegalArgumentException])
    assert(thrown.getMessage === "requirement failed: An end time can't be before start time!")
  }

  it should "have a toString() method which returns a string in the right format" in {
    assert(testRecord.toString == "[0] 12.03.2007 - 15:30 to 16:15 | 38 min (- 7 min) | alone, Prog2, Doing homework, -")
  }

  it should "return ' -' when comment is None" in {
    val record = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T16:00"),
      Some(LocalDateTime.parse("2007-03-12T16:00")), 0, alone = true, None, "SS19")
    assert(record.getCommentString === "-")
  }

  it should "return ' -' when study form is None" in {
    val record = Record(0, None, Some("Prog2"), LocalDateTime.parse("2007-03-12T16:00"),
      Some(LocalDateTime.parse("2007-03-12T16:00")), 0, alone = true, Some("commenting..."), "SS19")
    assert(record.getStudyFormString === "-")
  }

  it should "return ' -' when course is None" in {
    val record = Record(0, Some("Doing homework"), None, LocalDateTime.parse("2007-03-12T16:00"),
      Some(LocalDateTime.parse("2007-03-12T16:00")), 0, alone = true, Some("commenting..."), "SS19")
    assert(record.getCourseString === "-")
  }



  override def afterAll(): Unit = {
    dbTestController.deleteDatabase()
  }

}
