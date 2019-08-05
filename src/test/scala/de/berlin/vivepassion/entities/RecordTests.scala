package de.berlin.vivepassion.entities

import java.time.{LocalDate, LocalDateTime}

import org.scalatest.{BeforeAndAfter, FunSuite}

class RecordTests extends FunSuite with BeforeAndAfter {

  val record = Record(0, "Doing homework",
                        "Prog2",
                        LocalDateTime.parse("2007-12-03T15:30:00"),
                        LocalDateTime.parse("2007-12-03T16:15:00"),
                        7, true, " ", "SS19")

  test ("test record getDate method") {
    assert(record.getDate == LocalDate.parse("2007-12-03"))
  }

  test ("test record study session length") {
    assert(record.getSessionLength == 38l)
  }

  test ("test record toString method") {
    assert(record.toString == "[0] 12.03.2007 - 15:30 to 16:15 | 38 min | alone, Prog2, Doing homework, -")
  }

  // tests for record companion object ------------------------------------------------------------
  test ("test record mapping from string to record instance") {
    val temp = Record.fromLine("03.12.2007,15:30,16:15,7,Hausaufgaben machen,Allein,Prog2, ", 0)
    assert(record == temp)
  }

}
