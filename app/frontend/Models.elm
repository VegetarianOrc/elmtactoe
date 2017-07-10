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
            100

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


spaceContains : BoardSpace -> ( Float, Float ) -> Bool
spaceContains space point =
    let
        ( pointX, pointY ) =
            point

        maxX =
            space.x + space.width

        maxY =
            space.y + space.width

        inBounds min max val =
            (toFloat min) < val && val < (toFloat max)
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
    | ReceiveSVGCoords ( Float, Float )
