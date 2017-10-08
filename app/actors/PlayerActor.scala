package com.amazzeo.elmtactoe.actors

import akka.actor._
import com.amazzeo.elmtactoe.models._

object PlayerActor {

  def props(outSocket: ActorRef) = {
    Props(new PlayerActor(outSocket))
  }

}

class PlayerActor(out: ActorRef) extends Actor {

  private[this] var game: Option[ActorRef] = None

  def receive = {
    case JoinGame(gameName) =>
      GameCache.get(gameName) ! GameActor.Subscribe

    case subscribeResult: SubscibeResult =>
      if (subscribeResult.success) {
        game = Option(GameCache.get(subscribeResult.gameName))
      }
      out ! subscribeResult

    case playerMove: PlayerMove =>
      game.foreach(_ ! playerMove)

    case gameState: GameState =>
      out ! gameState

    case gameComplete: GameComplete =>
      out ! gameComplete

  }

}

