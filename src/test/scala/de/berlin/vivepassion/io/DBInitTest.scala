package de.berlin.vivepassion.io

import de.berlin.vivepassion.io.database.DBInit
import org.scalatest.{BeforeAndAfter, FunSuite}

class DBInitTest extends FunSuite with BeforeAndAfter {

  test ("create Course db table") (pending)

  test ("create Form db table") {
    DBInit.createStudyFormTable

  }

}
