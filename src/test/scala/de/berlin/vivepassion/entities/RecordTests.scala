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
    assert(record.getSessionLength === 38)
  }

  it should "have a toString() method which returns a string in the right format" in {
    assert(record.toString == "[0] 12.03.2007 - 15:30 to 16:15 | 38 min (- 7 min) | alone, Prog2, Doing homework, -")
  }



  "A record companion object" should "generate a record instance from a string" in {
    val temp = Record.fromLine("12.03.2007,15:30,16:15,7,Doing homework,alone,Prog2,", 0)
    assert(record == temp)
  }

}
