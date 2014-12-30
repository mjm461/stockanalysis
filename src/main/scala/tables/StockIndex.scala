package tables

import scala.slick.driver.H2Driver.simple._

object StockIndex extends GenericTable {
  override val tablename: String = "STOCK_INDEX"
  
  case class StockIndexRow(
    override val id: Option[Int] = None,
    symbol: String,
    updated: java.sql.Date = null ) extends TableRowWithId(id) {
  }

  class StockIndexDef(tag: Tag) extends TableWithId[StockIndexRow](tag, tablename ) {
    def symbol = column[String]("SYMBOL", O.NotNull)
    def updated = column[java.sql.Date]("UPDATED" , O.Nullable )
    def * = (id.?, symbol, updated) <> (StockIndexRow.tupled, StockIndexRow.unapply)
    //def nameIdx = index("u_name", (name), unique = true)
  }

  object table extends GenericTableWithId[StockIndexDef, StockIndexRow] {
    val table = TableQuery[StockIndexDef]
    val tablename: String = "STOCK_INDEX"
  }
}