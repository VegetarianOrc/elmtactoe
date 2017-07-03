module Main exposing (..)

import Html
import Models exposing (..)
import Update exposing (..)
import View exposing (..)


makeRow : Int -> Int -> List BoardSpace
makeRow yValue width =
    let
        makeBoardSpace index =
            { x = index * width
            , y = yValue
            , width = width
            , content = Empty
            }
    in
        List.map makeBoardSpace [ 0, 1, 2 ]


init : ( Model, Cmd Msg )
init =
    let
        width =
            306

        cellWidth =
            width // 3

        helper index =
            makeRow (index * cellWidth) cellWidth

        board =
            List.map helper [ 0, 1, 2 ]

        initialModel =
            { nextMove = Cross
            , board = board
            , winner = Nothing
            }
    in
        ( initialModel, Cmd.none )


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none


main : Program Never Model Msg
main =
    Html.program
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }
