/* eslint-disable react/prop-types */
import GameListElement from "../GameListElement/GameListElement";
import { useEffect, useState } from "react";

function GameList({games}) {
    return (
        <ul className="list bg-blue-400 rounded-b-box shadow-md">
            <li className="p-4 pb-2 text-3xl text-amber-50">All games</li>
            {games.map(game => <GameListElement key={game.id} game={game} />)}
        </ul>
    )
}

export default GameList;
