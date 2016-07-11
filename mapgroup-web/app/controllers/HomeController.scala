package controllers

import javax.inject._
import akka.actor._
import akka.stream._
import play.api._
import play.api.mvc._
import play.api.libs.streams._
import play.api.libs.json._
import com.github.nscala_time.time.Imports._

import actors._
import model._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (implicit system: ActorSystem, implicit val materializer: Materializer) extends Controller {

  implicit val myCustomCharset = Codec.javaSupported("iso-8859-1")

  val sourceActor = system.actorOf(SourceActor.props, "source-actor")

  val groups = List(new CharacGroup (id         = "GR001",
                                     groupDate  = new DateTime(),
                                     elements   = List("1", "2", "3"),
                                     affinities = List("sexo:M", "UF:SP")),
                    new CharacGroup (id         = "GR002",
                                     groupDate  = new DateTime(),
                                     elements   = List("1", "2", "3"),
                                     affinities = List("sexo:F", "UF:MG")),
                    new CharacGroup (id         = "GR003",
                                     groupDate  = new DateTime(),
                                     elements   = List("1", "2", "3"),
                                     affinities = List("idade:12", "sexo:M", "UF:SP")))

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def main = Action {
    Ok(views.html.main(groups))
  }

  def addGroup = Action {
    Ok(views.html.main(groups))
  }

  def wsGroup = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out, sourceActor))
  }

}
