package de.berlin.vivepassion.testspecs

import de.berlin.vivepassion.controller.{EntityController, EntityControllerInterface}
import de.berlin.vivepassion.io.database.{VPStatsDBController, VPStatsDBRepository}

/**
 * Object which provides essential fields for all test classes.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
object VPStatTestConfig {

  private val testDBPath: String = "jdbc:sqlite:src/test/resources/vpstats_test_db"
  val testCsvFilePath: String = "./src/main/resources/tables/Test_Semester_Table.csv"

  val dbTestController: VPStatsDBController = new VPStatsDBController(testDBPath)
  val dbTestRepository: VPStatsDBRepository = new VPStatsDBRepository(dbTestController)
  val dbTestEntityController: EntityControllerInterface = new EntityController(dbTestRepository)

}
