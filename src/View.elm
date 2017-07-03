module View exposing (..)

import Models exposing (..)
import Html exposing (..)
import Html.Attributes as HtmlAttr
import Svg exposing (..)
import Svg.Attributes exposing (..)
import Svg.Events exposing (..)
import Json.Decode as Decode


decodeClickLocation : Decode.Decoder Msg
decodeClickLocation =
    Decode.map OnTouch
        (Decode.map2
            (,)
            (Decode.at [ "offsetX" ] Decode.int)
            (Decode.at [ "offsetY" ] Decode.int)
        )


renderCross : ( Int, Int ) -> Int -> List (Svg msg)
renderCross point w =
    let
        ( x, y ) =
            point

        viewBoxStr =
            [ x, y, w, w ]
                |> List.map toString
                |> String.join " "

        bufferedX =
            x + 5

        bufferedY =
            y + 5

        newX =
            toString ((w + x) - 5)

        newY =
            toString ((w + y) - 5)
    in
        [ line [ x1 (toString bufferedX), y1 (toString bufferedY), x2 newX, y2 newY, stroke "blue" ] []
        , line [ x1 newX, y1 (toString bufferedY), x2 (toString bufferedX), y2 newY, stroke "blue" ] []
        ]


renderCircle : ( Int, Int ) -> Int -> List (Svg msg)
renderCircle point w =
    let
        ( x, y ) =
            point

        centerX =
            x + (w // 2)

        centerY =
            y + (w // 2)

        radius =
            w // 2 - 5

        centerXStr =
            toString centerX

        centerYStr =
            toString centerY
    in
        [ circle [ cx centerXStr, cy centerYStr, r (toString radius), fill "none", stroke "red", strokeWidth "2" ] [] ]


renderBoardSpace : BoardSpace -> List (Svg msg)
renderBoardSpace space =
    case space.content of
        Cross ->
            renderCross ( space.x, space.y ) space.width

        Circle ->
            renderCircle ( space.x, space.y ) space.width

        Empty ->
            let
                viewBoxStr =
                    [ space.x, space.y, space.width, space.width ]
                        |> List.map toString
                        |> String.join " "
            in
                [ rect [ viewBox viewBoxStr, width ((toString space.width) ++ "px") ] [] ]


renderBoard : List (List BoardSpace) -> Int -> List (Svg msg)
renderBoard board width =
    let
        cellWidth =
            width // 3

        verticals =
            [ line [ x1 (toString cellWidth), y1 "0", x2 (toString cellWidth), y2 (toString width), stroke "black" ] []
            , line [ x1 (toString (cellWidth * 2)), y1 "0", x2 (toString (cellWidth * 2)), y2 (toString width), stroke "black" ] []
            ]

        horizontals =
            [ line [ y1 (toString cellWidth), x1 "0", y2 (toString cellWidth), x2 (toString width), stroke "black" ] []
            , line [ y1 (toString (cellWidth * 2)), x1 "0", y2 (toString (cellWidth * 2)), x2 (toString width), stroke "black" ] []
            ]

        renderedSpaces =
            board
                |> List.concat
                |> List.map renderBoardSpace
    in
        List.concat (verticals :: horizontals :: renderedSpaces)


renderWinner : Maybe PlayerMove -> Html msg
renderWinner maybeWinner =
    case maybeWinner of
        Just symbol ->
            let
                svgInner point width =
                    case symbol of
                        Cross ->
                            renderCross point width

                        _ ->
                            --should never be empty, but handle it here
                            renderCircle point width
            in
                div []
                    [ svg [ viewBox "0 0 50 50", width "50px" ] (svgInner ( 0, 0 ) 50)
                    , Html.text " wins!"
                    ]

        Nothing ->
            div [] []


view : Model -> Html Msg
view model =
    div [ HtmlAttr.style [ ( "margin-left", "300px" ) ] ]
        [ svg
            [ viewBox "0 0 306 306", width "306px", on "click" decodeClickLocation ]
            (renderBoard model.board 306)
        , renderWinner model.winner
        ]
