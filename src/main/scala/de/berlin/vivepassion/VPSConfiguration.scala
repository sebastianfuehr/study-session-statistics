package de.berlin.vivepassion

import java.time.format.DateTimeFormatter
import java.util.Properties
import java.io.FileInputStream

class VPSConfiguration {

}
object VPSConfiguration {

  /** Path of the 'vivepassionstats.properties' file. */
  val PROPERTIES_PATH = "resources/vivepassionstats.properties"
  val properties: Properties = new Properties()

  /**
    * Loads all properties and prepares application launch.
    */
  def init(): Unit = {
    /** Loads the properties from the {@link PROPERTIES_PATH}. */
    VPSConfiguration.properties.load(new FileInputStream(VPSConfiguration.PROPERTIES_PATH))
  }

  // TODO implement method to have centralized scope of date time format
  def getDateTimeFormatter: DateTimeFormatter = {
    DateTimeFormatter.ofPattern(
      properties.getProperty("default_date_format")
      + "'T'"
      + properties.getProperty("default_time_format")
    )
  }

}