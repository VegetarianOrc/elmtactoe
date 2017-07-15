package com.amazzeo.elmtactoe.models

import io.circe._, generic.auto._, syntax._, parser._
import play.api.mvc.WebSocket.MessageFlowTransformer._

sealed trait BoardSpace
case object Empty extends BoardSpace
case object Cross extends BoardSpace
case object Circle extends BoardSpace

case class Player(name: String)

case class Game(
  nextMove: BoardSpace,
  board: Array[Array[BoardSpace]],
  player1: Player,
  player2: Player)

sealed trait WSMessage

case class JoinGame(gameName: String) extends WSMessage
case class PlayerMove(
  x: Int,
  y: Int,
  move: BoardSpace) extends WSMessage {
  require(move != Empty)
}

trait WSMessageCirce {

  implicit val wsMessageEncoder = new Encoder[WSMessage] {
    final def apply(wsMsg: WSMessage): Json = {
      wsMsg match {
        case join: JoinGame => Encoder[JoinGame].apply(join)
        case move: PlayerMove => Encoder[PlayerMove].apply(move)
      }
    }
  }

  implicit def wsMessageDecoder(wsMsg: WSMessage) = {
    wsMsg match {
      case join: JoinGame => Decoder[JoinGame]
      case move: PlayerMove => Decoder[PlayerMove]
    }
  }

  implicit val wsMessageFlowTransformer = stringMessageFlowTransformer.map[WSMessage] { str =>
    (decode[JoinGame](str).toOption orElse decode[PlayerMove](str).toOption) match {
      case None =>
        throw new Exception("oh god")
      case Some(res) => res
    }
  }

}
