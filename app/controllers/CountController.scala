package controllers

import javax.inject._
import play.api.libs.json._
import models.productoDAO
import models.producto

import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._

@Singleton
class CountController @Inject()(cc: ControllerComponents, productoDAO: productoDAO)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getProductos: Action[AnyContent] = Action.async {
    productoDAO.all().map{productos =>
      Ok(Json.toJson(productos))
    }.recover{
      case ex: Exception => InternalServerError(Json.obj("error" -> "error al obtener los productos"))
    }
  }

  def getProducto(id: Int): Action[AnyContent] = Action.async {
    productoDAO.get(id).map{
      case Some(producto) => Ok(Json.toJson(producto))
      case None => NotFound(Json.obj("error" -> s"producto no encontrado con el $id"))
    }.recover{
      case ex: Exception => InternalServerError(Json.obj("error" -> s"error al obtener el producto $id" ))
    }
  }

  def insert: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[producto].fold(
      errors => Future.successful(BadRequest(JsError.toJson(errors))),
      producto => {
        productoDAO.insert(producto).map { _ =>
          Created(Json.obj("mensaje" -> "producto insertado", "producto" -> Json.toJson(producto) ))
        }
      }
    )
  }

  def delete (id: Int): Action[AnyContent] = Action.async {
    productoDAO.delete(id).map { deletedCount =>
      if (deletedCount > 0){
        Ok(Json.obj("status" -> "Producto eliminado con éxito"))
      } else{
        NotFound
      }
    }.recover( {
      case ex: Exception => InternalServerError(Json.obj("error" -> "error al borrar el producto"))
    })
  }

  def update(id: Int): Action[JsValue] = Action.async(parse.json) {
    request =>
      request.body.validate[producto].fold(
        errors => Future.successful(BadRequest(JsError.toJson(errors))),
        producto => {
          val updatedCount = productoDAO.update(id, producto)
          updatedCount.map { updatedCount =>
            if (updatedCount > 0) Ok(Json.toJson(producto))
            else NotFound
          }.recover {
            case ex: Exception => InternalServerError(Json.obj("error" -> "Error al actualizar el producto"))
          }
        }
      )
  }

}
