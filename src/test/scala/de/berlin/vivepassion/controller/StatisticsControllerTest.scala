package de.berlin.vivepassion.controller

import de.berlin.vivepassion.testspecs.VPStatSpecDatabase

class StatisticsControllerTest extends VPStatSpecDatabase {


  "The StatisticsController" should "calculate the complete learning time alone" in {
    assert(StatisticsController.getLearningTimeAlone(testStudySessions, true) == 356.0)
  }


}
