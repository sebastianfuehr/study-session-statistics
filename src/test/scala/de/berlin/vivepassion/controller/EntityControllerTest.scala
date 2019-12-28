package de.berlin.vivepassion.controller

import de.berlin.vivepassion.testspecs.VPStatTestDB
import de.berlin.vivepassion.testspecs.VPStatTestConfig._

class EntityControllerTest extends VPStatTestDB {

  "EntityController" should "save a semester in the local database" in {
    assert (
      dbTestEntityController.saveSemesterIfNotExists("SS19")
      && !dbTestEntityController.saveSemesterIfNotExists("SS19")
    )
  }

  it should "save a course in the local database" in {
    assert (
      dbTestEntityController.saveCourseIfNotExists("courseX98Z")
      && !dbTestEntityController.saveCourseIfNotExists("courseX98Z")
    )
  }

  it should "save a form of studying in the local database" in {
    assert (
      dbTestEntityController.saveStudyFormIfNotExists("dancing")
      && !dbTestEntityController.saveStudyFormIfNotExists("dancing")
    )
  }

  it should "save a study day in the local database" in {
    assert (
      dbTestEntityController.saveStudyDayIfNotExists("24.06.1998")
      && !dbTestEntityController.saveStudyDayIfNotExists("24.06.1998")
    )
  }

}
