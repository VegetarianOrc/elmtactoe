package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.libs.streams.ActorFlow
import play.api.libs.circe._
import play.api.libs.json._
import akka.actor.ActorSystem
import akka.stream.Materializer
import com.amazzeo.elmtactoe.actors._
import com.amazzeo.elmtactoe.models._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) with WSMessageCirce with Circe {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def testsocket = WebSocket.accept[WSMessage, WSMessage] { req =>
    ActorFlow.actorRef { out =>
      PlayerActor.props(out)
    }
  }

}
