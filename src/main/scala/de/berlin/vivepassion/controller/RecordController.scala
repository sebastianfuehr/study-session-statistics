package de.berlin.vivepassion.controller

class RecordController {

}
object RecordController {
  def startNewSemster(): Boolean = { // TODO implement method
    val term: String = UserController.getUserInput("Which term is the new semester?")

    return false;
  }

  /**
    * Prompts the user for input in form of 'property=value' and tries to map these values to a new instance
    * of Record.
    * @param callParams String array of key-value-pairs to be cast into a new Record instance.
    */
  def addNewLearnSessionDialog(callParams: Array[String]): Unit = { // TODO implement method
    val params = callParams.map(e => {
      val entryTuple = e.split("=")
      (entryTuple(0), entryTuple(1))
    })
    for (entry <- params) {
      entry match {
        case ("date", date) =>
        case ("start", startTime) =>
        case ("end", endTime) =>
        case ("form", form) =>
        case ("course", course) =>
        case ("pause", pause) =>
      }
    }
    params.foreach(t => println(t._1 + t._2))
  }
}
