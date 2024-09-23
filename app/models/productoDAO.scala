package models

import javax.inject.{Inject, Singleton}
import models.{producto, productos}
import play.api.db.slick.DatabaseConfigProvider
import  slick.jdbc.JdbcProfile
// Sirve para interactuar con la base de datos
import scala.concurrent.{ExecutionContext, Future}
// Crea una instancia de la base de datos para interactuar con ella y sus tablas
@Singleton
class productoDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // Obtiene la configuración de la base de datos
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  // Obtiene el profile de la base de datos para interactuar con ella y sus tabla
  import profile.api._
  private val products = TableQuery[productos]
  // CRUD
  //Metodo para buscar todos los productos
  def all(): Future[Seq[producto]] = db.run(products.result)
  //Metodo para buscar un producto por su id
  def get(id: Int): Future[Option[producto]] = db.run(products.filter(_.id === id).result.headOption)
  // Metodo para insertar un nuevo producto
  def insert(producto: producto): Future[Int] = db.run(products += producto)
  // Metodo para borrar un producto
  def delete (id: Int): Future[Int] = db.run(products.filter(_.id === id).delete)
  // Metodo para actualizar un producto
  def update(id: Int, producto: producto): Future[Int] = db.run(products.filter(_.id === id).update(producto))
}
