package test

//import common._
import org.joda.time.{DateTime, Period }
import common.CSVParser

object Test {

 // val s2 = "aa,bb,cc\n11,22,33".split("\n").toList.map( x => CSVParser.apply(x) )
  class CsvData( header: List[String], data: List[List[String] ]){
    def apply( input: List[List[String] ] ) = input match{
      case x :: xs => new CsvData( input.head, input.tail )
      case _ => null
    }
  }
	val a = List(1,2,3)                       //> a  : List[Int] = List(1, 2, 3)
	a.head                                    //> res0: Int = 1
	a.tail                                    //> res1: List[Int] = List(2, 3)
	val b = List()                            //> b  : List[Nothing] = List()
	//b.head
	//b.tail
	
	//CsvData.apply( List( 'a','b','c' ) )
	
/*
  def getStockPriceFromInception( symbol: String ): String = {
    getStockPrice( symbol: String, DateTime.now, List('d', 'e', 'f'))
  }
  
  def getStockPriceFromDate( symbol: String, end: DateTime ): String = {
    getStockPrice( symbol: String, end, List('a', 'b', 'c'))
  }
  
  def getStockPrice(symbol : String, end: DateTime, vars: List[Char] ): String = {
    "http://ichart.finance.yahoo.com/table.csv?&s=" + symbol +
      "&"+ vars(0) + "=" + (end.getMonthOfYear-1) + // month is 0 based for some reason
      "&"+ vars(1) + "=" + end.getDayOfMonth +
      "&"+ vars(2) + "=" + end.getYearOfCentury +
      "&ignore=.csv"
    
    
  }
  
  
  val url = getStockPriceFromInception("AAPL")
 	val d = new DateTime(12, 8, 12, 0, 0)
  var url2 = getStockPriceFromDate("AAPL", d )
  
  val vp = getStockPriceFromInception(_)
  val vp2 = getStockPriceFromDate("AA", _ :DateTime )
  
  vp("AAPL")
  
  val i: Int = null
  */
  
}