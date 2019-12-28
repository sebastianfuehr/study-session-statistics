package de.berlin.vivepassion.testspecs

import de.berlin.vivepassion.controller.{EntityController, EntityControllerInterface}
import de.berlin.vivepassion.gui.Dialogues
import de.berlin.vivepassion.io.MockUserInteractionInstance
import de.berlin.vivepassion.io.database.{VPStatsDBController, VPStatsDBRepository}
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

/**
 * Trait which provides essential fields for all test classes.
 *
 * @author Sebastian Führ
 * @version 0.1
 */
trait VPStatSpec extends FlatSpec with BeforeAndAfterAll {

  val dbTestController: VPStatsDBController =
    new VPStatsDBController("jdbc:sqlite:src/test/resources/vpstats_test_db")
  val dbTestRepository: VPStatsDBRepository = new VPStatsDBRepository(dbTestController)
  val dbTestEntityController: EntityControllerInterface = new EntityController(dbTestRepository, new Dialogues(new MockUserInteractionInstance))

  dbTestController.deleteAllTables()
  dbTestController.createDatabase()
  dbTestController.clearAllTables()

}
