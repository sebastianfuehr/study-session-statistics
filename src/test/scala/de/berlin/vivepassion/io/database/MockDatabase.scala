package de.berlin.vivepassion.io.database

/**
 * Class which creates and fills a database for testing purposes.
 * @param dbController Configured instance of DBController.
 * @param dbRepository Configured instance of DBRepository.
 */
class MockDatabase(dbController: DBController, dbRepository: DBRepository) {

  // Set the database to state "zero"
  dbController.createDatabase
  dbController.clearAllTables

  //

}
