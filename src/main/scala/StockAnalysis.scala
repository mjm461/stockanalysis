import scala.slick.driver.H2Driver.simple._

import tables._
import common._
import scala.slick.jdbc.meta._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import org.joda.time.{ DateTime, Period, Days }

object StockAnalysis extends App {

  val db = StockDatabaseConnection.getConnection

  db.withSession {
    implicit session =>
      {
        val tableIndex = StockIndex.table.createAndGetTable
        val tablePrice = StockPrice.table.createAndGetTable
        val id: Int = StockIndex.table.insert(new StockIndex.StockIndexRow(None, "AAPL"))
        val rows = CSVParser.apply(new java.io.File("/home/mark/aapl.csv")).toList
        val mapper = StockPrice.getMapper(id, rows.head)
        val insertables = rows.tail.map(mapper.mapRow)
        // insertables.foreach { StockPrice.table.insert }  // one at a time

        StockPrice.table.insertAll(insertables)

        val r = for {
          p <- StockPrice.table.table.sortBy(_.date.asc)
          s <- p.sfk if s.id === id
        } yield (p.sid, p.id, s.symbol, p.date, p.adjClose)

        println("ID for inserts: " + id)
        r.list.foreach(println)

      }
  }
  //val symbol = "AAPL"

  /*
  def createTables = db.withSession{
    implicit session =>
      if (MTable.getTables(Stocks.TABLENAME).list.isEmpty) {
          (Stocks.db.ddl).create
      }
      
       if (MTable.getTables(StockPrice.TABLENAME).list.isEmpty) {
        (StockPrice.db.ddl).create
      }
  }
  
  def getStockPriceFromInceptionCSV(symbol: String) = {
    getStockPriceCSV(symbol: String, DateTime.now, List('d', 'e', 'f'))
  }

  def getStockPriceFromDateCSV(symbol: String, end: DateTime) = {
    getStockPriceCSV(symbol: String, end, List('a', 'b', 'c'))
  }

  def getStockPriceCSV(symbol: String, end: DateTime, vars: List[Char]) = {
    val url = "http://ichart.finance.yahoo.com/table.csv?&s=" + symbol +
      "&" + vars(0) + "=" + (end.getMonthOfYear - 1) + // month is 0 based for some reason
      "&" + vars(1) + "=" + end.getDayOfMonth +
      "&" + vars(2) + "=" + end.getYearOfCentury +
      "&ignore=.csv"
    io.Source.fromURL(url).mkString //.split("\n").toList
  }

  def getId(s: String) = db.withSession {
    implicit session =>
      if (MTable.getTables(Stocks.TABLENAME).list.isEmpty) {
        (Stocks.db.ddl).create
      }

      val q = for (c <- Stocks.db if c.symbol === symbol) yield c.id
      val l = q.list
      if (l.isEmpty)
        (Stocks.db returning Stocks.db.map(_.id)) += Stocks.StockRow(None, s)
      else
        l.head
  }
  
  createTables

  val id = getId(symbol)

  val maxSymbolDate: java.sql.Date = db.withSession { implicit session =>
    val r = for {
      p <- StockPrice.db if p.sid === id
    } yield p.date
    r.max.run.getOrElse(null)
  }

  val now = DateTime.now
  val daysSinceClose = List( 2, 0, 0, 0, 0, 0, 1 )
  val daysSinceCheck = Days.daysBetween( new DateTime( maxSymbolDate ),  
        new DateTime(now.year.get, now.monthOfYear().get, now.dayOfMonth.get, 0, 0, 0, 0) ).getDays
  println( "LAST: " + daysSinceCheck + " " + daysSinceClose(daysSinceCheck ))
  
  if (maxSymbolDate != null) {
    println( "MAX from local DB: " + maxSymbolDate )
    
    println( now.dayOfWeek.get)
    
  } else {
    val s: List[List[String]] = getStockPriceFromInceptionCSV(symbol).split("\n").toList.map(CSVParser.apply(_))
    val header = s.head; // First line of CSV defines columns
    val rows = s.tail; // Rest of CSV is data

    val mapper = StockPrice.getMapper(id, header)

    db.withSession { implicit session =>
      // Check if table exists
      if (MTable.getTables(StockPrice.TABLENAME).list.isEmpty) {
        (StockPrice.db.ddl).create
      }

      // Do the inserts
      rows.map(mapper.mapRow).foreach(StockPrice.db += _)

      val r = for {
        p <- StockPrice.db.sortBy(_.date.asc)
        s <- p.sfk if s.id === id
      } yield (p.sid, p.id, s.symbol, p.date, p.adjClose)

      r.list.foreach(println)

      val r2 = for {
        p <- StockPrice.db if p.sid === id
      } yield p.date
      println("MAX " + r2.max.run.getOrElse(null))

    }
  }
  
  */
}
