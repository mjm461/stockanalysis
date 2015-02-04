package tables

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import scala.slick.backend.DatabaseComponent

trait GenericTable {
  abstract class TableRowWithId(val id: Option[Int] = None)

  val tablename: String
  lazy val database = StockDatabaseConnection.getConnection

  abstract class TableWithId[A](tag: Tag, name: String) extends Table[A](tag: Tag, name) {
    def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  }

  abstract class GenericTable[T <: Table[A], A] {

    val table: TableQuery[T]

    def createAndGetTable(implicit session: Session) = session.withTransaction {
      if (MTable.getTables(tablename).list.isEmpty) {
        (table.ddl).create
      }
      table
    }

    def insert(entry: A)(implicit session: Session): Int = session.withTransaction {
      table += entry
      0
    }

    def insertAll(entries: List[A])(implicit session: Session) = session.withTransaction {
      table.insertAll(entries: _*)
    }

    def all: List[A] = database.withSession { implicit session =>
      table.list.map(_.asInstanceOf[A])
    }
  }

  abstract class GenericTableWithId[T <: TableWithId[A], A <: TableRowWithId] extends GenericTable[T, A] {
    override def insert(entry: A)(implicit session: Session): Int = session.withTransaction {
      table.returning(table.map(_.id)) += entry
    }
  }
}