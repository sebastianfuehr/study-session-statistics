package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.io.database.DBRepository

/** Controller class for study sessions. */
class RecordController(dbRepository: DBRepository) {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

}
