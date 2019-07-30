package de.berlin.vivepassion.controller

import java.io.FileInputStream
import java.util.Properties

import de.berlin.vivepassion.VPSConfiguration

object RecordController {

  val properties: Properties = new Properties()
  properties.load(new FileInputStream(VPSConfiguration.propertiesPath))

}
