package de.berlin.vivepassion.entities

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.testspecs.VPStatSpec

class RecordTests extends VPStatSpec {

  val record = Record(0, Some("Doing homework"),
                        Some("Prog2"),
                        LocalDateTime.parse("2007-03-12T15:30:00"),
                        Some(LocalDateTime.parse("2007-03-12T16:15:00")),
                        7, alone = true, None, "SS19")


  "A record" should "return a date as a string in the right format" in {
    assert(record.getDate == LocalDate.parse("2007-03-12"))
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
    assert(record.toString == "[0] 12.03.2007 - 15:30 to 16:15 | 38 min (- 7 min) | alone, Prog2, Doing homework, -")
  }

  it should "return ' -' when comment is None" in {
    val record = Record(0, Some("Doing homework"), Some("Prog2"), LocalDateTime.parse("2007-03-12T16:00"),
      Some(LocalDateTime.parse("2007-03-12T16:00")), 0, alone = true, None, "SS19")
    assert(record.getCommentString() === "-")
  }

  it should "return ' -' when study form is None" in {
    val record = Record(0, None, Some("Prog2"), LocalDateTime.parse("2007-03-12T16:00"),
      Some(LocalDateTime.parse("2007-03-12T16:00")), 0, alone = true, Some("commenting..."), "SS19")
    assert(record.getStudyFormString() === "-")
  }

  it should "return ' -' when course is None" in {
    val record = Record(0, Some("Doing homework"), None, LocalDateTime.parse("2007-03-12T16:00"),
      Some(LocalDateTime.parse("2007-03-12T16:00")), 0, alone = true, Some("commenting..."), "SS19")
    assert(record.getCourseString() === "-")
  }



  "A record companion object" should "generate a record instance from a string" in {
    val temp = Record.fromLine("12.03.2007,15:30,16:15,7,Doing homework,alone,Prog2,", 0)
    assert(record == temp)
  }

}
