package de.berlin.vivepassion.controller

import de.berlin.vivepassion.io.CSVFileLoader
import de.berlin.vivepassion.testspecs.VPStatSpec
import org.scalatest.BeforeAndAfterAll

class StatisticsControllerTest extends VPStatSpec with BeforeAndAfterAll {

  override def beforeAll() {
    dbTestController.clearAllTables()
    CSVFileLoader.importCsvIntoDatabase(testProperties.getProperty("test_csv_table_path"), dbTestEntityController)
  }



  "The StatisticsController" should "calculate the complete learning time alone" in {
    val statsTestController = new StatisticsController(dbTestRepository)
    assert(statsTestController.getLearningTimeAlone(alone = true) === 5.6)
  }

  it should "calculate the average learning time per day" in {
    val statsTestController = new StatisticsController(dbTestRepository)
    val result = BigDecimal(
      statsTestController.getAverageLearningTime()
    ).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    assert(result === 59.33)
  }

  it should "calculate the average learning time per day alone" in {
    val statsTestController = new StatisticsController(dbTestRepository)
    val result = BigDecimal(
      statsTestController.getAverageLearningTimeAlone(true)
    ).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    assert(result === 67.2)
  }


  override def afterAll() = dbTestController.clearAllTables()


}