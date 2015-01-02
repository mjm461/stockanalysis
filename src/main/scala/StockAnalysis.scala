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

        val (id: Int, updated: java.sql.Date) = {
          val query = for (c <- tableIndex if c.symbol === symbol) yield (c.id, c.updated)
          val results = query.list
          if (results.isEmpty) (-1, null)
          else results.head
        }

        val today = DateTime.now
        val yesterday = today.minusDays(1).dayOfYear().roundFloorCopy()

        val csvData: String = {
          if (updated == null)
            StockData.getStockPriceFromInceptionCSV(symbol)
          else if (Days.daysBetween(new DateTime(updated), new DateTime(yesterday)).getDays > 0)
            StockData.getStockPriceFromDateCSV(symbol, new DateTime(updated))
          else
            null
        }

        //val csvAll = CSVParser.apply(new java.io.File("/home/mark/aapl.csv")).toList
        //val csvData: String  = null

        if (csvData != null) {
          val csvAll = csvData.split("\n").toList.map(CSVParser.apply(_))
          val header = csvAll.head
          val data = csvAll.tail
          if (data != Nil) {
            val sid: Int = {
              if (id < 0)
                StockIndex.table.insert(new StockIndex.StockIndexRow(None, symbol, new java.sql.Date(yesterday.getMillis)))
              else {
                val q = for { l <- tableIndex if l.id === id } yield l.updated
                q.update(new java.sql.Date(yesterday.getMillis)) //val statement = q.updateStatement //val invoker = q.updateInvoker
                id
              }
            }
            val mapper = StockPrice.getMapper(sid, header)
            val insertables = data.map(mapper.mapRow)
            StockPrice.table.insertAll(insertables)
          }
        } else {
          println("Already exists and is updated")
        }
      }
  }
}
