package de.berlin.vivepassion.controller

import de.berlin.vivepassion.testspecs.VPStatSpec
import org.scalatest.BeforeAndAfterAll

class StatisticsControllerTest extends VPStatSpec with BeforeAndAfterAll {

  override def beforeAll() {
    dbTestController.clearAllTables()
    dbTestRepository.importCsvIntoDatabase(testProperties.getProperty("test_csv_table_path"), "WS18/19")
  }



  "The StatisticsController" should "calculate the complete learning time alone" in {
    val statsTestController = new StatisticsController(dbTestRepository)
    assert(statsTestController.getLearningTimeAlone(alone = true) === 5.33)
  }

  it should "calculate the average learning time per day" in {
    val statsTestController = new StatisticsController(dbTestRepository)
    assert(statsTestController.getAverageLearningTime() === 0.0)
  }


  override def afterAll() = dbTestController.clearAllTables()


}
