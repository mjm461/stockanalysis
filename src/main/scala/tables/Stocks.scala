package tables

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.{ ProvenShape, ForeignKeyQuery }

object Stocks {
  val TABLENAME = "STOCKS";

  lazy val db = TableQuery[StockTable]
  lazy val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
  
  object StockColumns {
    def symbol: String = "SYMBOL"
  }

  case class StockRow(id: Option[Int], symbol: String )

  class StockTable(tag: Tag)
    extends Table[(StockRow)](tag, TABLENAME) {
    def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def symbol: Column[String] = column[String](StockColumns.symbol)
    def * = (id.?, symbol ) <> (StockRow.tupled, StockRow.unapply)
  }
}