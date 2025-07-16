/* eslint-disable react/prop-types */
import GameListElement from "../GameListElement/GameListElement";
import { GameForList } from "../../types/Game";

type GameListProps = {
    games: GameForList[],
    listTitle: string
}

function GameList({games, listTitle} : GameListProps) {
    return (
        <ul className={`w-full list bg-blue-400 rounded-b-box ${games.length > 0 ? "" : "hidden"}`}>
            <li className="p-4 pb-2 text-3xl text-amber-50">{listTitle}</li>
            {games.map(game => <GameListElement key={game.id} game={game} />)}
        </ul>
    )
}

export default GameList;
