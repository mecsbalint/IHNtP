/* eslint-disable react/prop-types */
import GameListElement from "../GameListElement/GameListElement";

function GameList({games}) {

    return (
        <ul>
            {games.map(game => <GameListElement key={game.id} game={game} />)}
        </ul>
    )
}

export default GameList;
