package de.berlin.vivepassion.io.database

import de.berlin.vivepassion.VPSConfiguration

object TestDBController {

  val dbController = new DBController(VPSConfiguration.properties.getProperty("test_db_url"))

}
