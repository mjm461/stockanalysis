package common

import tables._
import common._
import scala.slick.jdbc.meta._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import org.joda.time.{ DateTime, Period, Days }

object StockData {
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
/*
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
  */
}