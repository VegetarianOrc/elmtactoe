module Main exposing (..)

import Html
import Models exposing (..)
import Update exposing (..)
import View exposing (..)


subscriptions : Model -> Sub Msg
subscriptions model =
    receiveSvgCoords ReceiveSVGCoords


main : Program Never Model Msg
main =
    Html.program
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }
