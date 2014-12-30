import scala.slick.driver.H2Driver.simple._

import tables._
import common._
import scala.slick.jdbc.meta._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import org.joda.time.{ DateTime, Period, Days }

object StockAnalysis extends App {
  /*
  def getId(s: String)(implicit session: Session) = session.withTransaction {

    val q = for (c <- StockIndex.table.table if c.symbol === symbol) yield (c.id, c.updated)
    val l = q.list
    if (l.isEmpty)
      ( -1, null )
    else
      l.head
  }
*/
  val db = StockDatabaseConnection.getConnection
  val symbol = "AAPL"

  db.withSession {
    implicit session =>
      {
        val tableIndex = StockIndex.table.createAndGetTable
        val tablePrice = StockPrice.table.createAndGetTable

        val (id: Int, updated) = {
          val query = for (c <- StockIndex.table.table if c.symbol === symbol) yield (c.id, c.updated)
          val results = query.list
          if (results.isEmpty) (-1, null)
          else results.head
        }

        val today = DateTime.now
        val yesterday = today.minusDays(1).dayOfYear().roundFloorCopy()

        val (csvData, updateTable: Boolean) = {
          if (updated == null) (StockData.getStockPriceFromInceptionCSV(symbol), true)
          else if (Days.daysBetween(new DateTime(updated), new DateTime(yesterday)).getDays > 0) (StockData.getStockPriceFromDateCSV(symbol, yesterday), true)
          else (null, false)
        }

        if (updateTable) {

          ///val csvAll = CSVParser.apply(new java.io.File("/home/mark/aapl.csv")).toList
          val csvAll = csvData.split("\n").toList.map(CSVParser.apply(_))
          val header = csvAll.head
          val data = csvAll.tail

          val sid: Int = {
            if (id < 0)
              StockIndex.table.insert(new StockIndex.StockIndexRow(None, symbol, new java.sql.Date(yesterday.getMillis)))
            else
              id
          }
          val mapper = StockPrice.getMapper(sid, header)
          val insertables = data.map(mapper.mapRow)
          StockPrice.table.insertAll(insertables)
        } else {
          println("Already exists and is updated")
        }

      }
  }
}
