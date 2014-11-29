package test

import parser.CSVParser

object CSVParserTest {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val s: Iterator[List[String]] = CSVParser.apply(new java.io.File( "/home/mark/aapl.csv"))
                                                  //> s  : Iterator[List[String]] = non-empty iterator

  s.foreach( println )                            //> List(Date, Open, High, Low, Close, Volume, Adj Close)
                                                  //| List(2014-11-12, 109.38, 111.43, 109.37, 111.25, 46505800, 111.25)
                                                  //| List(2014-11-11, 108.70, 109.75, 108.40, 109.70, 27442300, 109.70)
                                                  //| List(2014-11-10, 109.02, 109.33, 108.67, 108.83, 27195500, 108.83)
                                                  //| List(2014-11-07, 108.75, 109.32, 108.55, 109.01, 33691500, 109.01)
                                                  //| List(2014-11-06, 108.60, 108.79, 107.80, 108.70, 34968500, 108.70)
                                                  //| List(2014-11-05, 109.10, 109.30, 108.13, 108.86, 37435900, 108.39)
                                                  //| List(2014-11-04, 109.36, 109.49, 107.72, 108.60, 41574400, 108.13)
                                                  //| List(2014-11-03, 108.22, 110.30, 108.01, 109.40, 52282600, 108.93)
                                                  //| List(2014-10-31, 108.01, 108.04, 107.21, 108.00, 44639300, 107.53)
                                                  //| List(2014-10-30, 106.96, 107.35, 105.90, 106.98, 40654800, 106.52)
                                                  //| List(2014-10-29, 106.65, 107.37, 106.36, 107.34, 52687900, 106.88)
                                                  //| List(2014-10-28, 105.40, 106.74, 105.35, 106.74, 47939900, 106.28)
                                                  //| List(2014-10-27, 104.85, 105.48, 104.70, 105.11, 34187700, 104.66)
                                                  //| List(2014-10-24
                                                  //| Output exceeds cutoff limit.|
}