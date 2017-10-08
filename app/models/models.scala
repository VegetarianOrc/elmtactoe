package com.amazzeo.elmtactoe.models

import io.circe._, generic.auto._, syntax._, parser._, Encoder._, Decoder._
import play.api.mvc.WebSocket.MessageFlowTransformer._
import scala.util.{ Success, Failure }

sealed trait BoardSpace
case object Empty extends BoardSpace
sealed trait PlayerSpace extends BoardSpace
case object Cross extends PlayerSpace
case object Circle extends PlayerSpace

case class Player(name: String)

case class Game(
  nextMove: BoardSpace,
  board: Array[Array[BoardSpace]],
  player1: Player,
  player2: Player)

sealed trait WSMessage

case class JoinGame(gameName: String) extends WSMessage
case class SubscibeResult(gameName: String, success: Boolean, move: Option[PlayerSpace]) extends WSMessage
case class GameState(
  board: Array[Array[BoardSpace]],
  nextMove: PlayerSpace) extends WSMessage
case class GameComplete(
  catsGame: Boolean,
  winner: Option[PlayerSpace]) extends WSMessage

case class PlayerMove(
  x: Int,
  y: Int,
  move: PlayerSpace) extends WSMessage

trait WSMessageCirce {

  implicit val playerSpaceEncoder = encodeString.contramap[PlayerSpace] { playerSpace =>
    playerSpace match {
      case Circle => "circle"
      case Cross => "cross"
    }
  }

  implicit val baordSpaceEncoder = encodeString.contramap[BoardSpace] {
    space =>
      space match {
        case Circle => "circle"
        case Cross => "cross"
        case Empty => "empty"
      }
  }

  implicit val wsMessageEncoder = new Encoder[WSMessage] {
    final def apply(wsMsg: WSMessage): Json = {
      wsMsg match {
        case join: JoinGame => Encoder[JoinGame].apply(join)
        case move: PlayerMove => Encoder[PlayerMove].apply(move)
        case subscibeResult: SubscibeResult => Encoder[SubscibeResult].apply(subscibeResult)
        case gs: GameState => Encoder[GameState].apply(gs)
        case gc: GameComplete => Encoder[GameComplete].apply(gc)
      }
    }
  }

  implicit val decodePlayerSpace = decodeString.emap[PlayerSpace] { stringVal =>
    stringVal match {
      case "circle" => Right(Circle)
      case "cross" => Right(Cross)
      case _ => Left(s"Invalid player move: ${stringVal}")
    }

  }

  def decodeWsMessage(str: String): WSMessage = {
    (decode[JoinGame](str).toTry orElse decode[PlayerMove](str).toTry) match {
      case Failure(err) =>
        println(err)
        throw err
      case Success(res) =>
        res
    }
  }

  def encodeWsMessage(wsMessage: WSMessage): String = {
    wsMessageEncoder(wsMessage).noSpaces
  }

  implicit val wsMessageFlowTransformer = stringMessageFlowTransformer.map[WSMessage, WSMessage](decodeWsMessage _, encodeWsMessage _)

}
