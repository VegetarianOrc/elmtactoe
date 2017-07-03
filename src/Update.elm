module Update exposing (..)

import Models exposing (..)
import Debug
import Maybe.Extra


updateRow : ( Int, Int ) -> PlayerMove -> List BoardSpace -> ( Bool, List BoardSpace )
updateRow point nextContent row =
    let
        updateSpaceIfNeeded space =
            if (spaceContains space point) then
                case space.content of
                    Empty ->
                        ( True, { space | content = nextContent } )

                    _ ->
                        ( False, space )
            else
                ( False, space )

        ( updateStatuses, spaces ) =
            row
                |> List.map updateSpaceIfNeeded
                |> List.unzip

        didUpdate =
            List.foldl (\x y -> x || y) False updateStatuses
    in
        ( didUpdate, spaces )


updateBoard : ( Int, Int ) -> Model -> Model
updateBoard point model =
    let
        ( updateStatuses, newBoard ) =
            model.board
                |> List.map (updateRow point model.nextMove)
                |> List.unzip

        didUpdate =
            List.foldl (\x y -> x || y) False updateStatuses

        nextMove =
            case model.nextMove of
                Cross ->
                    Circle

                _ ->
                    Cross
    in
        if didUpdate then
            { model | board = newBoard, nextMove = nextMove }
        else
            model


detectWinner : Model -> Maybe PlayerMove
detectWinner model =
    let
        isAllSameMove spaces move =
            spaces
                |> List.map .content
                |> List.all (\n -> n == move)

        isWinner spaces =
            let
                winner =
                    isAllSameMove spaces Cross
                        || isAllSameMove spaces Circle
            in
                ( winner, spaces )

        rows =
            model.board
                |> List.map isWinner

        getColumn index =
            model.board
                |> List.map (List.drop index)
                |> List.map List.head
                |> Maybe.Extra.values

        columns =
            [ 0, 1, 2 ]
                |> List.map getColumn
                |> List.map isWinner

        diag1 =
            let
                ele1 =
                    model.board
                        |> List.head
                        |> Maybe.andThen List.head

                ele2 =
                    model.board
                        |> List.drop 1
                        |> List.head
                        |> Maybe.map (List.drop 1)
                        |> Maybe.andThen List.head

                ele3 =
                    model.board
                        |> List.drop 2
                        |> List.head
                        |> Maybe.map (List.drop 2)
                        |> Maybe.andThen List.head
            in
                Maybe.Extra.values [ ele1, ele2, ele3 ]

        diag2 =
            let
                ele1 =
                    model.board
                        |> List.drop 2
                        |> List.head
                        |> Maybe.andThen List.head

                ele2 =
                    model.board
                        |> List.drop 1
                        |> List.head
                        |> Maybe.map (List.drop 1)
                        |> Maybe.andThen List.head

                ele3 =
                    model.board
                        |> List.head
                        |> Maybe.map (List.drop 2)
                        |> Maybe.andThen List.head
            in
                Maybe.Extra.values [ ele1, ele2, ele3 ]

        diags =
            [ diag1, diag2 ]
                |> List.map isWinner

        winningSymbol =
            [ rows, columns, diags ]
                |> List.concat
                |> List.filter Tuple.first
                |> List.map Tuple.second
                |> List.head
                |> Maybe.andThen List.head
                |> Maybe.map .content
    in
        winningSymbol


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        OnTouch point ->
            case model.winner of
                Just winner ->
                    ( model, Cmd.none )

                Nothing ->
                    let
                        p =
                            Debug.log "point: " point

                        updated =
                            updateBoard point model

                        newWinner =
                            detectWinner updated
                    in
                        ( { updated | winner = newWinner }, Cmd.none )
