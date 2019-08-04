package de.berlin.vivepassion.io.database

import java.time.{LocalDate, LocalDateTime}

import de.berlin.vivepassion.VPSConfiguration
import de.berlin.vivepassion.entities.{Record, StudyDay}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.sqlite.SQLiteException

class DBRepositoryTest extends FunSuite with BeforeAndAfter {

  val dbController: DBController = new DBController(VPSConfiguration.properties.getProperty("test_db_url"))
  val dbRepository: DBRepository = new DBRepository(dbController)
  val mockDB: MockDatabase = new MockDatabase(dbController, dbRepository)

  test ("test try inserting NULL attribute in study_day table") {
    dbController.createStudyDayTable
    val thrown = intercept[Exception] {
      dbRepository.saveStudyDay(StudyDay(0, LocalDate.parse("2019-10-10"), 0, null))
    }
    assert(thrown.isInstanceOf[SQLiteException])
  }

  test ("test inserting NULL attribute in record table") {
    dbController.createRecordTable
    val thrown = intercept[Exception] {
      dbRepository.saveRecord(Record(null,
        "Introduction to Programming with Java", LocalDateTime.parse("2019-10-10T15:00"),
        LocalDateTime.parse("2019-10-10T15:45"), 5, true, ":-)", 0, "SS19"))
    }
    assert(thrown.isInstanceOf[SQLiteException])
  }

  test ("test retrieve all study sessions alone") (pending)

  test ("test retrieve all study sessions in a group") (pending)

  test ("test retrieve all study sessions for a specific day") (pending)

  test ("test retrieve all study sessions of a specific month") (pending)

  test ("test retrieve all study sessions of a specific semester") (pending)

  test ("test retrieve all semesters") (pending)

  test ("test retrieve all courses") (pending)

  test ("test retrieve all forms of study") (pending)

  test ("test retrieve all study sessions") (pending)

  test ("test retrieve all study days") (pending)

  test ("test retrieve all study days of a specific semester") (pending)

  test ("test retrieve a specific study day") (pending)

  after {
    dbController.clearAllTables
  }

}
