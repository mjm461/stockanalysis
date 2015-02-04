import scala.slick.driver.H2Driver.simple._

import tables._
import common._
import scala.slick.jdbc.meta._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import org.joda.time.{ DateTime, Period, Days }
import StockIndex.StockIndexRow

object StockAnalysis extends App {
  def getIndex(symbol: String)(implicit session: Session) = session.withTransaction {
    val query = for (c <- StockIndex.table.createAndGetTable if c.symbol === symbol) yield c
    val results = query.list
    if (results.isEmpty) null
    else results.head
  }

  def getStockUpdates(row: StockIndexRow, yesterday: DateTime) = {
    if (row == null)
      StockData.getStockPriceFromInceptionCSV(symbol)
    else if (Days.daysBetween(new DateTime(row.updated), new DateTime(yesterday)).getDays > 0)
      StockData.getStockPriceFromDateCSV(symbol, new DateTime(row.updated))
    else
      null
  }

  class CsvHeaderData( input: List[List[String] ] ){
    def header() = if ( input.isEmpty ) null else input.head
    def data() = if ( input.isEmpty || input.tail.isEmpty ) null else input.tail
  }
  
  val db = StockDatabaseConnection.getConnection
  val symbol = "AAPL"

  db.withSession {
    implicit session =>
      {
        val tableIndex = StockIndex.table.createAndGetTable
        val tablePrice = StockPrice.table.createAndGetTable

        val row = getIndex(symbol)

        val yesterday = DateTime.now().minusDays(1).dayOfYear().roundFloorCopy()

        val csvData: String = getStockUpdates( row, yesterday )
        
        if (csvData != null) {
          val csvAll = new CsvHeaderData( csvData.split("\n").toList.map(CSVParser.apply(_)) )
          if (csvAll.data() != null) { // data is in tail
            val sid: Int = {
              if (row == null)
                StockIndex.table.insert(new StockIndexRow(None, symbol, new java.sql.Date(yesterday.getMillis)))
              else {
                val q = for { l <- tableIndex if l.id === row.id } yield l.updated
                q.update(new java.sql.Date(yesterday.getMillis))
                row.id.get
              }
            }
            val mapper = StockPrice.getMapper(sid, csvAll.header()) // header is in head
            StockPrice.table.insertAll(csvAll.data().map(mapper.mapRow))
          }
        } else {
          println("Already exists and is updated")
        }
      }
  }
}
