module Models exposing (..)


type PlayerMove
    = Empty
    | Cross
    | Circle


type alias BoardSpace =
    { x : Int
    , y : Int
    , width : Int
    , content : PlayerMove
    }


spaceContains : BoardSpace -> ( Int, Int ) -> Bool
spaceContains space point =
    let
        ( pointX, pointY ) =
            point

        maxX =
            space.x + space.width

        maxY =
            space.y + space.width

        inBounds min max val =
            min < val && val < max
    in
        (inBounds space.x maxX pointX)
            && (inBounds space.y maxY pointY)


isSameSpace : BoardSpace -> BoardSpace -> Bool
isSameSpace b1 b2 =
    b1.content == b2.content


type alias Model =
    { nextMove : PlayerMove
    , board : List (List BoardSpace)
    , winner : Maybe PlayerMove
    }


type Msg
    = OnTouch ( Int, Int )
