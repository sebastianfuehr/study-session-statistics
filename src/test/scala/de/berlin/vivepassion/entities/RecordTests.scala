package de.berlin.vivepassion.entities

import java.time.{LocalDate, LocalDateTime}

import org.scalatest.{BeforeAndAfter, FunSuite}

class RecordTests extends FunSuite with BeforeAndAfter {

  var record: Record = _

  before {
    record = Record("Hausaufgaben machen",
                    "Prog2",
                    LocalDateTime.parse("2007-12-03T15:30:00"),
                    LocalDateTime.parse("2007-12-03T16:15:00"),
                    7, true, " ")
  }

  test ("test record getDate method") {
    assert(record.getDate == LocalDate.parse("2007-12-03"))
  }

  test ("test record study session length") {
    assert(record.getSessionLength == 38l)
  }

  // tests for record companion object ------------------------------------------------------------
  test ("test record application properties path") {
    assert(Record.PROPERTIES_PATH == "resources/vivepassionstats.properties")
  }

  test ("test record mapping from string to record instance") {
    val temp = Record.fromLine("12/03/2007,Di,15:30,16:15,7,Hausaufgaben machen,Allein,Prog2, ")
    assert(record == temp)
  }

}
