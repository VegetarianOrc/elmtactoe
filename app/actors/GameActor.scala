package com.amazzeo.elmtactoe.actors

import akka.actor._
import com.amazzeo.elmtactoe.models._
import com.amazzeo.elmtactoe.gameutils._

object GameActor {
  case object Subscibe
  case object SubscibeSuccess
  case class SubscibeError(err: Throwable)
  case class GameState(
    board: Array[Array[BoardSpace]],
    nextMove: PlayerSpace)
}

case object GameFullException extends Exception("This game is full.")

class GameActor() extends Actor with GameUtils {
  import GameActor._

  var crossPlayer: Option[ActorRef] = None
  var circlePlayer: Option[ActorRef] = None
  var board: Array[Array[BoardSpace]] = newBoard()
  var nextMove: PlayerSpace = Cross

  def receive = {
    case Subscibe =>
      subcribePlayer(sender())
  }

  def subcribePlayer(player: ActorRef) = {
    if (crossPlayer.isEmpty) {
      crossPlayer = Option(player)
      player ! SubscibeSuccess
    } else if (circlePlayer.isEmpty) {
      circlePlayer = Option(player)
      player ! SubscibeSuccess
    } else {
      player ! SubscibeError(GameFullException)
    }
  }

  def broadcast[T](msg: T) = {
    crossPlayer.foreach(_ ! msg)
    circlePlayer.foreach(_ ! msg)
  }

}
