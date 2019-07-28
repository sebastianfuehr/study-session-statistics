package de.berlin.vivepassion

import java.io.FileInputStream
import java.time.format.DateTimeFormatter
import java.util.Properties

object VPSConfiguration {

  /** Path of the 'vivepassionstats.properties' file. */
  val PROPERTIES_PATH = "./src/main/resources/vivepassionstats.properties"
  val properties: Properties = new Properties
  VPSConfiguration.properties.load(new FileInputStream(VPSConfiguration.PROPERTIES_PATH))

  // TODO implement method to have centralized scope of date time format
  def getDateTimeFormatter: DateTimeFormatter = {
    DateTimeFormatter.ofPattern(
      properties.getProperty("default_date_format")
      + "'T'"
      + properties.getProperty("default_time_format")
    )
  }

  /**
    * Prints all options of the "vivepassionstats.properties" file on the console.
    */
  def printApplicationProperties: Unit = {
    val props = new Properties()
    props.load(new FileInputStream(PROPERTIES_PATH))
    props.forEach((k, v) => println(k+":\t"+v))
  }

}