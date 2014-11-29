package tables

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.{ ProvenShape, ForeignKeyQuery }

object StockPrice {
  val TABLENAME = "STOCK_PRICE";

  lazy val db = TableQuery[StockPriceTable]
  lazy val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

  def order(in: List[String]): List[Int] = in match {
    case x :: xs if x.equals(StockPriceColumns.date) => 0 :: order(xs)
    case x :: xs if x.equals(StockPriceColumns.open) => 1 :: order(xs)
    case x :: xs if x.equals(StockPriceColumns.high) => 2 :: order(xs)
    case x :: xs if x.equals(StockPriceColumns.low) => 3 :: order(xs)
    case x :: xs if x.equals(StockPriceColumns.close) => 4 :: order(xs)
    case x :: xs if x.equals(StockPriceColumns.volume) => 5 :: order(xs)
    case x :: xs if x.equals(StockPriceColumns.adjClose) => 6 :: order(xs)
    case _ => Nil
  }

  def getMapper(sid: Int, in: List[String]): StockPriceMapper = new StockPriceMapper(sid, order(in.map(_.replace(" ", "").toUpperCase)))

  class StockPriceMapper(sid: Int, order: List[Int]) {
    def mapRow(in: List[String]) =
      if (in.length != order.length) throw new Exception("Length do not match" + in.length + "->" + order.length)
      else new StockPriceRow( None, sid, new java.sql.Date(format.parse(in(order(0))).getTime),
        in(order(1)).toDouble, in(order(2)).toDouble, in(order(3)).toDouble,
        in(order(4)).toDouble, in(order(5)).toDouble, in(order(6)).toDouble)
  }

  object StockPriceColumns {
    def symbol: String = "SYMBOL"
    def date: String = "DATE"
    def open: String = "OPEN"
    def high: String = "HIGH"
    def low: String = "LOW"
    def close: String = "CLOSE"
    def volume = "VOLUME"
    def adjClose = "ADJCLOSE"
  }

  case class StockPriceRow( id: Option[Int], sid: Int,date: java.sql.Date, open: Double,
                      high: Double, low: Double, close: Double, volume: Double, adjClose: Double)

  class StockPriceTable(tag: Tag)
    extends Table[(StockPriceRow)](tag, TABLENAME) {
    def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def sid: Column[Int] = column[Int]("SID")
    def date: Column[java.sql.Date] = column[java.sql.Date](StockPriceColumns.date)
    def open: Column[Double] = column[Double](StockPriceColumns.open)
    def high: Column[Double] = column[Double](StockPriceColumns.high)
    def low: Column[Double] = column[Double](StockPriceColumns.low)
    def close: Column[Double] = column[Double](StockPriceColumns.close)
    def volume: Column[Double] = column[Double](StockPriceColumns.volume)
    def adjClose: Column[Double] = column[Double](StockPriceColumns.adjClose)
    def sfk = foreignKey("SID", sid, Stocks.db)(_.id)

    def * = ( id.?, sid, date, open, high, low, close, volume, adjClose) <> (StockPriceRow.tupled, StockPriceRow.unapply)
  }
}