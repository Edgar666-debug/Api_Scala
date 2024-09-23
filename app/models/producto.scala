package models

import play.api.libs.json._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

case class producto(
  id: Int,
  title: String,
  description: String,
  precio: Double
                   )
object producto {
  implicit val productoFormat: OFormat[producto] = Json.format[producto]
}

class productos(tag: Tag) extends Table[producto](tag, "productos") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def title = column[String]("title")

  def description = column[String]("description")

  def precio = column[Double]("precio")


  def * : ProvenShape[producto] = (id, title, description, precio).mapTo[producto]
}