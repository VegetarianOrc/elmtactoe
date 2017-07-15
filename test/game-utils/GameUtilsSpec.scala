package com.amazzeo.elmtactoe.gameutils

import com.amazzeo.elmtactoe.models._
import org.scalatest._

class GameUtilsSpec extends FunSpec with Matchers with GameUtils {
  describe("GameUtils") {
    describe("isBoardFull") {
      it("should return false if all spaces are Empty") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Empty, Empty),
          Array(Empty, Empty, Empty),
          Array(Empty, Empty, Empty))

        isBoardFull(board) shouldBe false
      }

      it("should return false if one or more spaces are Empty") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Empty, Circle),
          Array(Empty, Empty, Empty),
          Array(Empty, Empty, Empty))

        isBoardFull(board) shouldBe false
      }

      it("should return true if all spaces are not Empty") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Circle, Cross, Circle),
          Array(Cross, Circle, Cross),
          Array(Cross, Circle, Cross))

        isBoardFull(board) shouldBe true
      }

    }

    describe("detectWinner") {
      it("should return none if there are no moves") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Empty, Empty),
          Array(Empty, Empty, Empty),
          Array(Empty, Empty, Empty))

        detectWinner(board) shouldBe None
      }

      it("should return the winner if the top row is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Cross, Cross, Cross),
          Array(Empty, Empty, Empty),
          Array(Empty, Empty, Empty))

        detectWinner(board) shouldBe Some(Cross)
      }

      it("should return the winner if the middle row is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Empty, Empty),
          Array(Circle, Circle, Circle),
          Array(Empty, Empty, Empty))

        detectWinner(board) shouldBe Some(Circle)
      }

      it("should return the winner if the bottom row is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Empty, Empty),
          Array(Empty, Empty, Empty),
          Array(Circle, Circle, Circle))

        detectWinner(board) shouldBe Some(Circle)
      }

      it("should return the winner if the first column is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Circle, Empty, Empty),
          Array(Circle, Empty, Empty),
          Array(Circle, Empty, Empty))

        detectWinner(board) shouldBe Some(Circle)
      }

      it("should return the winner if the middle column is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Cross, Empty),
          Array(Empty, Cross, Empty),
          Array(Empty, Cross, Empty))

        detectWinner(board) shouldBe Some(Cross)
      }

      it("should return the winner if the last column is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Empty, Circle),
          Array(Empty, Empty, Circle),
          Array(Empty, Empty, Circle))

        detectWinner(board) shouldBe Some(Circle)
      }

      it("should return the winner if the top-bottom diagonal is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Circle, Empty, Empty),
          Array(Empty, Circle, Empty),
          Array(Empty, Empty, Circle))

        detectWinner(board) shouldBe Some(Circle)
      }

      it("should return the winner if the bottom-top diagonal is all one player") {
        val board: Array[Array[BoardSpace]] = Array(
          Array(Empty, Empty, Cross),
          Array(Empty, Cross, Empty),
          Array(Cross, Empty, Empty))

        detectWinner(board) shouldBe Some(Cross)
      }
    }
  }
}
