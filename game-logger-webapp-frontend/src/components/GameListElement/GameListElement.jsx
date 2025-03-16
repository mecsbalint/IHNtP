/* eslint-disable react/prop-types */

function GameListElement({game}) {

    return (
        <li>
            <p>{game.name}</p>
            <p>{game.developer}</p>
            <p>{game.publisher}</p>
        </li>
    )
}

export default GameListElement;
