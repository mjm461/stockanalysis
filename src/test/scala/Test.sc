package test

import org.joda.time.{DateTime, Period }
import common.CSVParser

object Test {

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

}