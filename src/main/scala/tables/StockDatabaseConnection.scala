package tables

import scala.slick.driver.H2Driver.simple._

object StockDatabaseConnection {
  protected lazy val database = Database.forURL("jdbc:h2:./stocks", driver = "org.h2.Driver")
  def getConnection = database
}