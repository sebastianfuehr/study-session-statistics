package de.berlin.vivepassion.controller

import de.berlin.vivepassion.VPSConfiguration
import org.scalatest.{BeforeAndAfter, FunSuite}

class RecordControllerTests extends FunSuite with BeforeAndAfter {

  // tests for record companion object ------------------------------------------------------------
  test ("test record application properties path") {
    assert(VPSConfiguration.PROPERTIES_PATH == "resources/vivepassionstats.properties")
  }

}
